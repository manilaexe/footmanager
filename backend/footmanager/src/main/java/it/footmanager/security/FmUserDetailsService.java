package it.footmanager.security;

import it.footmanager.entity.Utente;
import it.footmanager.repository.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Carica l'utente dal DB per Spring Security.
 * Mappa il ruolo in un'authority del tipo {@code ROLE_STAFF}, {@code ROLE_ALLENATORE}, ecc.
 */
@Service
@RequiredArgsConstructor
public class FmUserDetailsService implements UserDetailsService {

    private final UtenteRepository utenteRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato: " + username));

        return User.builder()
                .username(utente.getUsername())
                .password(utente.getPasswordHash())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + utente.getRuolo().getNome().name())))
                .disabled(!utente.isAttivo())
                .build();
    }
}
