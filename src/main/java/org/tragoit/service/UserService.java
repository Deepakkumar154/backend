package org.tragoit.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tragoit.dto.AgentDto;
import org.tragoit.dto.AgentRequestDto;
import org.tragoit.dto.LoginRequestDto;
import org.tragoit.dto.UserDto;
import org.tragoit.exception.AppException;
import org.tragoit.model.Agent;
import org.tragoit.model.EmailDetails;
import org.tragoit.model.Role;
import org.tragoit.model.User;
import org.tragoit.repository.AgentRepository;
import org.tragoit.repository.RoleRepository;
import org.tragoit.repository.UserRepository;

import java.nio.CharBuffer;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AgentRepository agentRepository;
    private final EmailService emailService;


    public UserDto login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(CharBuffer.wrap(loginRequestDto.getPassword()), user.getPassword())) {
            return mapToUserDto(user);
        }
        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public UserDto agentLogin(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        Agent agent = agentRepository.findById(user.getId())
                .orElseThrow(() -> new AppException("User is not an agent", HttpStatus.NOT_FOUND));

        if (Boolean.FALSE.equals(agent.getIsVerified())) {
            throw new AppException("Agent is not verified", HttpStatus.BAD_REQUEST);
        }

        if (passwordEncoder.matches(CharBuffer.wrap(loginRequestDto.getPassword()), user.getPassword())) {
            return mapToUserDto(user);
        }
        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }


    public UserDto saveUser(UserDto userDto) {
        Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());

        if (optionalUser.isPresent()) {
            throw new AppException("Email already exists", HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Role roleUser = roleRepository.findByName("ROLE_CUSTOMER");
        user.setRole(roleUser);
        User savedUser = userRepository.save(user);
        return mapToUserDto(savedUser);
    }

    @Transactional
    public UserDto saveAgentUser(AgentRequestDto userDto) {
        Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());

        if (optionalUser.isPresent()) {
            throw new AppException("Email already exists", HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Role roleUser = roleRepository.findByName("ROLE_AGENT");
        user.setRole(roleUser);
        User savedUser = userRepository.save(user);

        Agent agent = new Agent();
        agent.setCompany(userDto.getCompany());
        agent.setContact(userDto.getContact());
        agent.setAddress(userDto.getAddress());
        agent.setFoundOn(userDto.getFoundOn());
        agent.setGovtId(userDto.getGovtId());
        agent.setGovtIdType(userDto.getGovtIdType());
        agent.setGstNumber(userDto.getGstNumber());
        agent.setPolicyDocument(userDto.getPolicyDocument());
        agent.setIsVerified(false);
        agent.setUser(savedUser);
        agentRepository.save(agent);

        emailService.sendEmail(EmailDetails.builder()
                .messageBody("Registration Successful with mail id: "+"tragoittravels@gmail.com")
                .recipient("aanchalsingh5882@gmail.com")
                .subject("REGISTRATION SUCCESS").build());
        return mapToUserDto(savedUser);
    }

    @Transactional
    public Agent saveAgentForUser(Long userId, AgentDto userAgentDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (!user.getRole().getName().equalsIgnoreCase("ROLE_AGENT")) {
            throw new RuntimeException("User does not have the role of 'agent'");
        }

        Agent agent = new Agent();
        agent.setUser(user); // Use the same ID

        agent.setCompany(userAgentDto.getCompany());
        agent.setContact(userAgentDto.getContact());
        agent.setAddress(userAgentDto.getAddress());
        agent.setFoundOn(userAgentDto.getFoundOn());
        agent.setGovtId(userAgentDto.getGovtId());
        agent.setGovtIdType(userAgentDto.getGovtIdType());
        agent.setGstNumber(userAgentDto.getGstNumber());
        agent.setPolicyDocument(userAgentDto.getPolicyDocument());

        return agentRepository.save(agent);
    }

    @Transactional
    public Agent updateAgentDetails(Long userId, Agent agentDetails) {
        // Retrieve the existing Agent by the User ID (which is also the Agent ID)
        Agent existingAgent = agentRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Agent not found with user id: " + userId));

        // Update the Agent-specific details
        existingAgent.setCompany(agentDetails.getCompany());
        existingAgent.setContact(agentDetails.getContact());
        existingAgent.setAddress(agentDetails.getAddress());
        existingAgent.setFoundOn(agentDetails.getFoundOn());
        existingAgent.setGovtId(agentDetails.getGovtId());
        existingAgent.setGovtIdType(agentDetails.getGovtIdType());
        existingAgent.setGstNumber(agentDetails.getGstNumber());
        existingAgent.setPolicyDocument(agentDetails.getPolicyDocument());

        // Save the updated Agent entity
        return agentRepository.save(existingAgent);
    }

    public List<Agent> getAllAgents() {
        return agentRepository.findAll();
    }

    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToUserDto)
                .toList();
    }

    private UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public UserDto findUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        return mapToUserDto(user);
    }

}
