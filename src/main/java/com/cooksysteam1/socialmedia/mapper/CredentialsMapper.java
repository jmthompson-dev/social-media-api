package com.cooksysteam1.socialmedia.mapper;

import com.cooksysteam1.socialmedia.entity.model.request.CredentialsDto;
import com.cooksysteam1.socialmedia.entity.model.response.CredentialsResponseDto;
import com.cooksysteam1.socialmedia.entity.resource.Credentials;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CredentialsMapper {
    Credentials requestToEntity(CredentialsDto credentialsRequestDto);

    List<Credentials> requestsToEntities(List<CredentialsDto> credentialsRequestDtos);

    CredentialsResponseDto entityToResponse(Credentials credentials);

    List<CredentialsResponseDto> entitiesToResponseDtos(List<Credentials> credentials);

}
