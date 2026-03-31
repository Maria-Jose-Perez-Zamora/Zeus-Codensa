package core.service;

import dependencies.dto.UserRequestDTO;
import dependencies.dto.UserResponseDTO;
import core.model.Role;
import core.model.User;
import core.validator.UserValidator;
import dependencies.persistence.entity.UserEntity;
import dependencies.persistence.repository.UserRepository;
import dependencies.persistence.mapper.EntityToModelMapper;
import dependencies.persistence.mapper.ModelToEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testRegisterUser_Success() {
        UserRequestDTO request = new UserRequestDTO("Ana", "user.test1-a@escuelaing.edu.co", "123456", "Defensa", 3, null, Role.PLAYER);
        
        UserEntity savedEntity = new UserEntity();
        savedEntity.setName("Ana");
        savedEntity.setEmail("user.test1-a@escuelaing.edu.co");
        savedEntity.setRole(Role.PLAYER);

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedEntity);
        doNothing().when(userValidator).validateForRegistration(request);

        UserResponseDTO response = userService.registerUser(request);
        
        assertNotNull(response);
        assertEquals("Ana", response.getName());
        assertEquals("user.test1-a@escuelaing.edu.co", response.getEmail());
        assertEquals(Role.PLAYER, response.getRole());
        
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void testGetAllUsers() {
        UserEntity e1 = new UserEntity();
        e1.setName("Ana");
        e1.setEmail("user.test1-a@escuelaing.edu.co");
        e1.setRole(Role.PLAYER);
        
        UserEntity e2 = new UserEntity();
        e2.setName("Pepe");
        e2.setEmail("user.test2-a@escuelaing.edu.co");
        e2.setRole(Role.PLAYER);
        
        List<UserEntity> entityList = new ArrayList<>();
        entityList.add(e1);
        entityList.add(e2);
        
        when(userRepository.findAll()).thenReturn(entityList);
        
        List<UserResponseDTO> users = userService.getAllUsers();
        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }
}
