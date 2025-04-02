package edu.tcu.cs.hogwartsartifactsonline.hogwartsuser;

import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService  {
    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<HogwartsUser> findAll(){
        return userRepository.findAll();
    }

    public HogwartsUser findById(Integer id) {
        return this.userRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("User", id));
    }

    public HogwartsUser save(HogwartsUser user) {
        return this.userRepository.save(user);
    }


    public HogwartsUser update(Integer id, HogwartsUser userUpdate) {
        HogwartsUser oldUser = this.findById(id); // too a short cut by using a findbyid method defined up there

        oldUser.setUsername(userUpdate.getUsername());
        oldUser.setEnabled(userUpdate.isEnabled());
        oldUser.setRoles(userUpdate.getRoles());

        return this.userRepository.save(oldUser);
    }

    public void delete(Integer id) {
        this.findById(id);
        this.userRepository.deleteById(id);
    }

}
