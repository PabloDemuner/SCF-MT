package com.scfapi.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.scfapi.dto.ContatoDTO;
import com.scfapi.model.Contato;

@Mapper(config = ScfMapperConfig.class)
public interface ContatoMapper {

	Contato toEntity(ContatoDTO dto);
	
	ContatoDTO toDTO(Contato entity);
	
	List<Contato> map(List<ContatoDTO> listDTO);

    List<ContatoDTO> mapByListObj(List<Contato> list);
}
