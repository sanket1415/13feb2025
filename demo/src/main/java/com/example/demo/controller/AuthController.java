package com.example.demo.controller;

import com.example.demo.Model.Student.Student;
import com.example.demo.Model.Teacher.Teacher;
import com.example.demo.dto.LoginRequest;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000") // ✅ Allow frontend requests
public class AuthController {

    @Autowired
    private TeacherRepository teacherRepository;  // ✅ Injected Repository

    @Autowired
    private StudentRepository studentRepository;  // ✅ Injected Repository

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        // ✅ Check if user is a teacher
        Teacher teacher = teacherRepository.findByemail(email); // ✅ Fixed method name
        if (teacher != null && teacher.getPassword().equals(password)) { // 🔥 No Hashing
            return ResponseEntity.ok(Map.of(
                    "name", teacher.getFirstName() + " " + teacher.getLastName(),
                    "email", teacher.getEmail(),
                    "qualification", teacher.getQualification(),
                    "subject", teacher.getSubject(),
                    "gender", teacher.getGender()
            ));
        }

        // ✅ Check if user is a student
        Student student = studentRepository.findByemail(email); // ✅ Fixed method name
        if (student != null && student.getPassword().equals(password)) { // 🔥 No Hashing
            return ResponseEntity.ok(Map.of(
                    "email", student.getEmail(),
                    "name", student.getFirstName() + " " + student.getLastName(),
                    "dob", student.getDob(),
                    "gender", student.getGender(),
                    "rollno", student.getRollno()
            ));
        }

        // ❌ If neither teacher nor student is found, return an error
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid email or password"));
    }
}
