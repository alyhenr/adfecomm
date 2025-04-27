package com.adfecomm.adfecomm.util;

import com.adfecomm.adfecomm.payload.ListResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListResponseBuilder {
    @Autowired
    private ModelMapper modelMapper;

    private Integer pageNumber;
    private Integer pageSize;
    private String sortBy;
    private String sortOrder;

    public static ListResponseBuilder create() {
        return new ListResponseBuilder();
    }

    public ListResponse createListResponse (Page<?> pageOfEntity, Class toMap) {
        List<?> listModel = pageOfEntity.getContent().stream()
                .map(c -> modelMapper.map(c, toMap))
                .toList();
        ListResponse listResponse = new ListResponse();
        listResponse.setContent(listModel);
        listResponse.setPageNumber(pageOfEntity.getNumber());
        listResponse.setPageSize(pageOfEntity.getSize());
        listResponse.setTotalElements(pageOfEntity.getTotalElements());
        listResponse.setTotalPages(pageOfEntity.getTotalPages());
        listResponse.setLastPage(pageOfEntity.isLast());

        return listResponse;
    }

    public Pageable buildPage() {
        Sort sortByOrderBy = sortOrder.equalsIgnoreCase("asc")
                ?  Sort.by(sortBy).ascending()
                :  Sort.by(sortBy).descending();

        return PageRequest.of(pageNumber, pageSize, sortByOrderBy);
    }

    private ListResponseBuilder() {
    }

    public ListResponseBuilder PageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    public ListResponseBuilder PageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }
    public ListResponseBuilder SortBy(String sortBy) {
        this.sortBy = sortBy;
        return this;
    }
    public ListResponseBuilder SortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }
}
