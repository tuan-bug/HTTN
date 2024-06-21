package com.be.service.impl;

import com.be.entity.Test;
import com.be.entity.TestUser;
import com.be.entity.Users;
import com.be.repository.TestRepository;
import com.be.repository.TestUserRepository;
import com.be.repository.UserRepository;
import com.be.service.TestUserService;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TestUserServiceImpl implements TestUserService {
    private final TestUserRepository testUserRepository;
    private final TestRepository testRepository;
    private final UserRepository userRepository;

    @Override
    public String addUserToTheTest(String testId, Users[] users) {
        Test test = testRepository.findById(testId).get();
        List<TestUser> testUsers = testUserRepository.getTestUserByTestId(testId);
        for (TestUser testUser : testUsers) {
            // Lấy user id của TestUser hiện tại
            String testUserId = testUser.getUser().getId();

            // Kiểm tra xem user id có nằm trong mảng Users hay không
            boolean userExistsInUsersArray = Arrays.stream(users).anyMatch(user -> user.getId().equals(testUserId));

            // Nếu user id không nằm trong mảng Users, thêm TestUser vào danh sách kết quả
            if (!userExistsInUsersArray) {
                testUserRepository.delete(testUser);
            }
        }
        for (Users user : users) {
            boolean userAlreadyEnrolled = testUserRepository.existsByUserAndTest(user, test);
            if (userAlreadyEnrolled) {
                continue; // Skip this user if already enrolled
            }
            TestUser testUser = new TestUser();
            String newGuid = UUID.randomUUID().toString();
            testUser.setId(newGuid);
            testUser.setUser(user);
            testUser.setTest(test);
            testUserRepository.save(testUser);
        }
        return "Add user to the test Successfully";
    }

    @Override
    public List<Users> getUsersFromTheTest(String testId) {
        List<TestUser> testUsers = testUserRepository.getTestUserByTestId(testId);
        List<Users> users = new ArrayList<>();
        for (TestUser testUser : testUsers) {
            Users user = userRepository.findById(testUser.getUser().getId()).get();
            users.add(user);
        }
        return users;
    }

    @Override
    public List<TestUser> findAllByUser(String userId) {
        return testRepository.findAllByUserId(userId);
    }

    @Override
    public List<TestUser> findAllByTestId(String testId) {
        return testUserRepository.getTestUserByTestId(testId);
    }

    @Override
    public TestUser findTestUserByTestIdAndUserId(Test test, Users user) {
        return testUserRepository.findByTestAndUser(test,user);
    }

    @Override
    public void saveResult(TestUser testUser) {
        testUserRepository.save(testUser);
    }

    @Override
    public Map<String, Long> getTestCompletionStatistics() {
        long completedCount = testUserRepository.countByResultIsNotNull();
        long notCompletedCount = testUserRepository.countByResultIsNull();

        Map<String, Long> stats = new HashMap<>();
        stats.put("completed", completedCount);
        stats.put("notCompleted", notCompletedCount);

        return stats;
    }
}
