package com.example.demo.dto;

import java.util.List;

public class ProductPageResponse {

    private List<ProductDTO> content;
    private int totalPages;
    private long totalElements;
    private int number;
    private int size;
    private boolean last;
    private boolean first;

    public List<ProductDTO> getContent() { return content; }
    public void setContent(List<ProductDTO> content) { this.content = content; }

    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }

    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }

    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public boolean isLast() { return last; }
    public void setLast(boolean last) { this.last = last; }

    public boolean isFirst() { return first; }
    public void setFirst(boolean first) { this.first = first; }
}