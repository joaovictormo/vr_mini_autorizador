package com.vr.miniautorizador.repositories;

import com.vr.miniautorizador.models.Cartao;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartaoRepository extends MongoRepository<Cartao, UUID> {}
