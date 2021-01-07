package org.apz.curso.service;


import java.util.List;
import java.util.stream.Collectors;

import org.apz.curso.model.Rol;
import org.apz.curso.model.Usuario;
import org.apz.curso.repository.RolRepository;
import org.apz.curso.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

   @Autowired
   UsuarioRepository usuarioRepository;
   
   @Autowired
   RolRepository rolRepository;
   
   
   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Usuario usuario = usuarioRepository.findByUsuario(username);
       List<Rol> roles = rolRepository.findByUsuario(usuario.getId());
       List<GrantedAuthority> authorities = roles.stream()
    		   .map(r -> new SimpleGrantedAuthority(r.getAuthority())).collect(Collectors.toList());
       
       return new org.springframework.security.core.userdetails.User(usuario.getUsuario(), usuario.getPassword(), authorities);
   }
}