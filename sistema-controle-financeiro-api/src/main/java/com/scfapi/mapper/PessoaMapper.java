package com.scfapi.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.scfapi.dto.PessoaDTO;
import com.scfapi.model.Pessoa;

@Mapper(config = ScfMapperConfig.class)
public interface PessoaMapper {
	//PessoaMapper INSTANCE = Mappers.getMapper( PessoaMapper.class );
	
	Pessoa pessoaDTOtoPessoa(PessoaDTO pessoa);

	PessoaDTO pessoaToPessoaDTO(Pessoa obj);

    List<Pessoa> map(List<PessoaDTO> listDTO);

    List<PessoaDTO> mapByListObj(List<Pessoa> list);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
        // ignora os campos nulos
    void updateFromDto(PessoaDTO dto, @MappingTarget Pessoa obj);

}
