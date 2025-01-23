package com.ohgiraffers.ukki.user.model.dto;

public class SearchDTO {

        private String searchWord;
        private long searchCount;

        public  SearchDTO(){}
    public SearchDTO(String searchWord, long searchCount) {
        this.searchWord = searchWord;
        this.searchCount = searchCount;
    }

    // Getter and Setter
    public String getSearchWord() {
        if (searchWord == null) {
            System.out.println("searchWord is null");
        }
        return searchWord;
    }


    public void setSearchWord(String searchWord) {
            this.searchWord = searchWord;
        }

        public long getSearchCount() {
            return searchCount;
        }

        public void setSearchCount(long searchCount) {
            this.searchCount = searchCount;
        }

    @Override
    public String toString() {
        return "SearchDTO{" +
                "searchWord='" + searchWord + '\'' +
                ", searchCount=" + searchCount +
                '}';
    }

}


