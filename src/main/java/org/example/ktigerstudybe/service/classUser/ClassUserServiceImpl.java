package org.example.ktigerstudybe.service.classUser;

import org.example.ktigerstudybe.dto.req.ClassUserRequest;
import org.example.ktigerstudybe.dto.resp.ClassUserResponse;
import org.example.ktigerstudybe.model.ClassEntity;
import org.example.ktigerstudybe.model.ClassUser;
import org.example.ktigerstudybe.model.User;
import org.example.ktigerstudybe.repository.ClassRepository;
import org.example.ktigerstudybe.repository.ClassUserRepository;
import org.example.ktigerstudybe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassUserServiceImpl implements ClassUserService {

    @Autowired
    private ClassUserRepository classUserRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private UserRepository userRepository;

    private ClassUserResponse toResponse(ClassUser cu) {
        ClassUserResponse res = new ClassUserResponse();
        res.setClassUserId(cu.getClassUserId());
        res.setClassId(cu.getClassEntity().getClassId());
        res.setClassName(cu.getClassEntity().getClassName());
        res.setUserId(cu.getUser().getUserId());
        res.setUserFullName(cu.getUser().getFullName());
        res.setJoinedAt(cu.getJoinedAt());
        return res;
    }

    @Override
    public List<ClassUserResponse> getAll() {
        return classUserRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ClassUserResponse getById(Long id) {
        ClassUser cu = classUserRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Not found ClassUserID: " + id));
        return toResponse(cu);
    }

    @Override
    public ClassUserResponse create(ClassUserRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        ClassEntity classEntity = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new IllegalArgumentException("Class not found"));

        ClassUser cu = ClassUser.builder()
                .user(user)
                .classEntity(classEntity)
                .build();

        return toResponse(classUserRepository.save(cu));
    }

    @Override
    public void delete(Long id) {
        if (!classUserRepository.existsById(id)) {
            throw new IllegalArgumentException("Cannot delete: Not found ClassUserID " + id);
        }
        classUserRepository.deleteById(id);
    }

    @Override
    public List<ClassUserResponse> getByUser(Long userId) {
        return classUserRepository.findByUser_UserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClassUserResponse> getByClass(Long classId) {
        return classUserRepository.findByClassEntity_ClassId(classId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
