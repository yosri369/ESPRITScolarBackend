package com.example.user.service;

import com.example.user.entity.*;
import com.example.user.repository.PendingTeacherRepository;
import com.example.user.repository.RoleRepository;
import com.example.user.repository.StudentRepository;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService; // ✅ Inject
    private final PendingTeacherRepository pendingTeacherRepository;


    /**
     * Converts a validated PendingStudent into a permanent User and Student profile.
     * Username = CIN, Email = firstname.lastname@example.com, Password = CIN (encoded)
     */
    public void createStudentFromPending(PendingStudent pendingStudent) {
        // Create and populate User
        User user = new User();

        String cin = pendingStudent.getCin();
        String email = pendingStudent.getFirstName().toLowerCase() + "." +
                pendingStudent.getLastName().toLowerCase() + "@esprit.com";

        user.setUsername(pendingStudent.getRegistrationNumber());
        user.setEmail(email);                        // Email = firstname.lastname@example.com
        user.setPassword(passwordEncoder.encode(cin));  // Password = CIN encoded

        // Basic personal info
        user.setFirstname(pendingStudent.getFirstName());
        user.setLastname(pendingStudent.getLastName());
        user.setBirthdate(pendingStudent.getBirthdate());
        user.setGender(pendingStudent.getGender());
        user.setPhone(pendingStudent.getPhone());
        user.setAddress(pendingStudent.getAddress());
        user.setCin(cin);
        user.setActive(true);

        // Assign Role
        Role studentRole = roleRepository.findByRoleType(RoleType.STUDENT)
                .orElseThrow(() -> new RuntimeException("Role STUDENT not found"));
        user.setRole(studentRole);

        // Create and attach Student profile
        Student student = new Student();
        student.setUser(user);  // one-to-one link
        student.setRegistrationNumber(pendingStudent.getRegistrationNumber());
        student.setEnrollmentDate(pendingStudent.getEnrollmentDate());
        student.setStatus("active");

        // Link user to student profile
        user.setStudentProfile(student);

        // Persist the User (and student through cascade if enabled, otherwise separately)
        userRepository.save(user);
        System.out.println("✅ New student created: " + email);
// ✅ Send email to the real form-provided email with system credentials
        emailService.sendStudentAccountInfoEmail(
                pendingStudent.getEmail(),       // form-provided email
                email,
                cin,
                pendingStudent.getRegistrationNumber()
        );

        //emailService.sendCredentialsEmail(email, cin, cin);


        System.out.println("✅ New student created:");
        System.out.println("   Username: " + cin);
        System.out.println("   Email: " + email);
        System.out.println("   Password: CIN");
    }

    public void createTeacherFromPending(PendingTeacher pendingTeacher) {
        String universityEmail = pendingTeacher.getFirstName().toLowerCase() + "." +
                pendingTeacher.getLastName().toLowerCase() + "@esprit.com";

        String cin = pendingTeacher.getCin();
        String registrationNumber = "TEA-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String rawPassword = cin;

        User user = new User();
        user.setUsername(registrationNumber);         // Username = registrationNumber
        user.setEmail(universityEmail);               // System email
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setFirstname(pendingTeacher.getFirstName());
        user.setLastname(pendingTeacher.getLastName());
        user.setBirthdate(pendingTeacher.getDateEmbauche()); // or another date field
        user.setGender("N/A"); // if missing
        user.setPhone(pendingTeacher.getPhone());
        user.setAddress(pendingTeacher.getAddress());
        user.setCin(cin);
        user.setActive(true);

        Role teacherRole = roleRepository.findByRoleType(RoleType.TEACHER)
                .orElseThrow(() -> new RuntimeException("Role TEACHER not found"));
        user.setRole(teacherRole);

        Teacher teacher = new Teacher();
        teacher.setUser(user);
        teacher.setDateEmbauche(pendingTeacher.getDateEmbauche());
        teacher.setGrade(pendingTeacher.getGrade());
        teacher.setSpeciality(pendingTeacher.getSpeciality());
        teacher.setFullTime(pendingTeacher.isFullTime());

        user.setTeacherProfile(teacher);

        userRepository.save(user);

        emailService.sendTeacherAccountInfoEmail(
                pendingTeacher.getEmail(),     // personal email
                universityEmail,               // system esprit email
                registrationNumber,            // username
                rawPassword                    // password = CIN
        );

        pendingTeacherRepository.delete(pendingTeacher);

        System.out.println("✅ Teacher account created: " + universityEmail);
    }

}
