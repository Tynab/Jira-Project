package com.nohit.jira_project.service.Impl;

import java.util.*;

import javax.transaction.*;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;

import com.nohit.jira_project.model.*;
import com.nohit.jira_project.repository.*;
import com.nohit.jira_project.service.*;

import static com.nohit.jira_project.constant.AttributeConstant.*;
import static java.util.Collections.*;
import static org.slf4j.LoggerFactory.*;

@Service
@Transactional
public class KhachHangServiceImpl implements KhachHangService, UserDetailsService {
    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Fields
    private Logger log = getLogger(KhachHangServiceImpl.class);

    @Override
    public List<KhachHang> getDsKhachHang() {
        // TODO Auto-generated method stub
        return khachHangRepository.findAll();
    }

    @Override
    public KhachHang getKhachHang(int id) {
        // TODO Auto-generated method stub
        return khachHangRepository.findById(id).orElse(null);
    }

    @Override
    public KhachHang getKhachHang(String email) {
        // TODO Auto-generated method stub
        return khachHangRepository.findByemail(email);
    }

    @Override
    public void saveKhachHang(KhachHang khachHang) {
        // TODO Auto-generated method stub
        khachHangRepository.save(khachHang);
    }

    @Override
    public void deleteKhachHang(int id) {
        // TODO Auto-generated method stub
        khachHangRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Query data of user
        var user = khachHangRepository.findByemail(username);
        // Check user exists
        if (user == null) {
            log.error("User not found");
            throw new UsernameNotFoundException("User not found");
        } else {
            log.info("User found: {}", username);
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getMatKhau(),
                    singleton(new SimpleGrantedAuthority(ROLE_PREFIX + user.getVaiTro().toUpperCase())));
        }
    }
}
