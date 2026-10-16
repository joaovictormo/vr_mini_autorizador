package com.vr.miniautorizador.mappers;

import com.vr.miniautorizador.dtos.CartaoDTO;
import com.vr.miniautorizador.models.Cartao;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartaoMapper {
  CartaoDTO map(Cartao source);
}
