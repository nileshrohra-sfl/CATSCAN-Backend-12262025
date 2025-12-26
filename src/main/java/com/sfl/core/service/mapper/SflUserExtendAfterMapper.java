package com.sfl.core.service.mapper;

import com.sfl.core.domain.Authority;
import com.sfl.core.domain.SflUserExtend;
import com.sfl.core.repository.AuthorityRepository;
import com.sfl.core.repository.SflUserExtendRepository;
import com.sfl.core.service.dto.SflUserExtendDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public class SflUserExtendAfterMapper {
    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private SflUserExtendRepository sflUserExtendRepository;

    @AfterMapping
    public void loadPrivateFields(@MappingTarget SflUserExtend newUser, SflUserExtendDTO sflUserExtendDTO) {
        Long id = sflUserExtendDTO.getId();
        if (id != null && sflUserExtendRepository.existsById(id)) {
            SflUserExtend dbUser = sflUserExtendRepository.getOne(newUser.getId());
            newUser.setPassword(dbUser.getPassword());
            newUser.setStatus(dbUser.getStatus());
            newUser.setActivationKey(dbUser.getActivationKey());
            newUser.setResetKey(dbUser.getResetKey());
            newUser.setResetDate(dbUser.getResetDate());
        }
    }

    @AfterMapping
    public void entityToString(@MappingTarget SflUserExtendDTO dto, SflUserExtend entity) {
        dto.setAuthorities(entitySetToStringSet(entity.getAuthorities()));
    }

    @AfterMapping
    public void stringToEntity(@MappingTarget SflUserExtend entity, SflUserExtendDTO dto) {
        entity.setAuthorities(stringSetToEntitySet(dto.getAuthorities()));
    }

    public Set<Authority> stringSetToEntitySet(Set<String> authoritiesAsString) {
        Set<Authority> authorities = new HashSet<>();
        if(authoritiesAsString !=null){
            for (String s : authoritiesAsString) {
                Optional<Authority> authority = authorityRepository.findByName(s);
                authority.ifPresent(authorities::add);
            }
        }
        return authorities;
    }

    public Set<String> entitySetToStringSet(Set<Authority> set) {
        return set.stream().map(Authority :: getName).collect(Collectors.toSet());
    }
}

