package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Page Result DTO
 * Generic pagination result wrapper
 *
 * @param <T> The type of items in the page
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    
    /**
     * List of items in current page
     */
    private List<T> items;
    
    /**
     * Total number of items
     */
    private Long total;
    
    /**
     * Current page number (1-based)
     */
    private Integer page;
    
    /**
     * Page size
     */
    private Integer size;
    
    /**
     * Total number of pages
     */
    private Integer totalPages;
    
    /**
     * Has next page
     */
    private Boolean hasNext;
    
    /**
     * Has previous page
     */
    private Boolean hasPrevious;
    
    /**
     * Create a page result
     *
     * @param items List of items
     * @param total Total count
     * @param page Current page
     * @param size Page size
     * @param <T> Item type
     * @return PageResult instance
     */
    public static <T> PageResult<T> of(List<T> items, Long total, Integer page, Integer size) {
        int totalPages = (int) Math.ceil((double) total / size);
        PageResult<T> result = new PageResult<>();
        result.setItems(items);
        result.setTotal(total);
        result.setPage(page);
        result.setSize(size);
        result.setTotalPages(totalPages);
        result.setHasNext(page < totalPages);
        result.setHasPrevious(page > 1);
        return result;
    }
}
