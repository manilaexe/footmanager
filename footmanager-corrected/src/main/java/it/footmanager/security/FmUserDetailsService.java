package it.footmanager.security;

import it.footmanager.entity.Utente;
import it.footmanager.repository.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FmUserDetailsService implements UserDetailsService {

    private final UtenteRepository utenteRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato: " + username));

        // getRuolo().getNomeRuolo() — es. "STAFF", "ALLENATORE", "GIOCATORE"
        String ruolo = utente.getRuolo() != null ? utente.getRuolo().getNomeRuolo() : "GIOCATORE";

        return User.builder()
                .username(utente.getUsername())
                .password(utente.getPassword())   // campo "password" nel DB
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + ruolo.toUpperCase())))
                .build();
    }
}
