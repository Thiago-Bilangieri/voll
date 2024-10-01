package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.domain.usuario.DadosLoginUsuario;
import med.voll.api.domain.usuario.Usuario;
import med.voll.api.infra.security.TokenService;
import med.voll.api.infra.security.DadosTokenJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;


    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity login(@RequestBody @Valid DadosLoginUsuario dadosLoginUsuario) {

        var authenticationToken = new UsernamePasswordAuthenticationToken(dadosLoginUsuario.login(), dadosLoginUsuario.password());
        var authentication = manager.authenticate(authenticationToken);
        var tokenJWT = tokenService.generateToken((Usuario) authentication.getPrincipal());


        return ResponseEntity.ok(new DadosTokenJwt(tokenJWT));

    }
}
