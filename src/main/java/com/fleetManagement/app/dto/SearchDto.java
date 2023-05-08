package com.fleetManagement.app.dto;

import com.fleetManagement.app.exception.BadRequestException;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Objects;

import static com.fleetManagement.app.commun.CoreConstant.Exception.*;
import static com.fleetManagement.app.commun.CoreConstant.Pagination.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchDto implements Serializable {

    private String keyword;

    private Integer page;
    private Integer size;

    public Integer getPage() {
        return (Objects.isNull(page) ? DEFAULT_PAGE_NUMBER : page);
    }

    public Integer getSize() {
        return (Objects.isNull(size) ? DEFAULT_PAGE_SIZE : size);
    }

    public String getKeyword() {
        return (Objects.isNull(keyword) ? StringUtils.EMPTY : keyword);
    }

    public void validate() throws BadRequestException {


        if (Objects.nonNull(page))
            if (this.page < 0)
                throw new BadRequestException(null, new BadRequestException(), PAGINATION_PAGE_NUMBER, null);

        if (Objects.nonNull(size)) {
            if (this.size <= 0)
                throw new BadRequestException(null, new BadRequestException(), PAGINATION_PAGE_SIZE_MIN, null);

            if (this.size > MAX_PAGE_SIZE)
                throw new BadRequestException(null, new BadRequestException(), PAGINATION_PAGE_SIZE_MAX, new Object[]{MAX_PAGE_SIZE});
        }
    }
}
