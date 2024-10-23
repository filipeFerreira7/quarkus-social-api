package io.github.filipeFerreira7.quarkussocial.domain.repository;

import io.github.filipeFerreira7.quarkussocial.domain.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped // cria uma instancia dessa classe dentro do contexto de aplicacao para usar onde quiser
public class UserRepository implements PanacheRepository<User> {

}
