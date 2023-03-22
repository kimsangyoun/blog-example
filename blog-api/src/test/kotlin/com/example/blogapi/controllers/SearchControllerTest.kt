package com.example.blogapi

import com.example.blogapi.api.dto.RankResponseDto
import com.example.blogapi.api.service.SearchService
import com.example.blogdomain.entity.search.Search
import com.example.blogapi.utils.IntegrationTest
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import java.util.*

@IntegrationTest
class SearchControllerTest(
    private val mockMvc: MockMvc
) {
    @MockkBean
    private lateinit var searchService: SearchService

    @Test
    fun `파라미터 테스트_정상`() {
        val testSearch ="title"

        every {
            searchService.saveSearch(testSearch)
        } just Runs

        val paramMap: MultiValueMap<String, String> = LinkedMultiValueMap()
        paramMap.add("pageNumber", "1")
        paramMap.add("pageSize", "10")
        paramMap.add("sort", "recency")
        paramMap.add("query", testSearch)
        mockMvc.perform(
            get("/api/v1/search")
                .params(paramMap)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    }

    @Test
    fun `파라미터 테스트_비정상_페이지사이즈_MAX_케이스`() {
        val testSearch ="title"

        every {
            searchService.saveSearch(testSearch)
        } just Runs

        val paramMap: MultiValueMap<String, String> = LinkedMultiValueMap()
        paramMap.add("pageNumber", "1")
        paramMap.add("pageSize", "51")
        paramMap.add("sort", "recency")
        paramMap.add("query", testSearch)
        mockMvc.perform(
            get("/api/v1/search")
                .params(paramMap)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("페이지 크기는 50 이하이여야 합니다."))
    }
    @Test
    fun `파라미터 테스트_비정상_페이지사이즈_MIN_케이스`() {
        val testSearch ="title"

        every {
            searchService.saveSearch(testSearch)
        } just Runs

        val paramMap: MultiValueMap<String, String> = LinkedMultiValueMap()
        paramMap.add("pageNumber", "1")
        paramMap.add("pageSize", "0")
        paramMap.add("sort", "recency")
        paramMap.add("query", testSearch)
        mockMvc.perform(
            get("/api/v1/search")
                .params(paramMap)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("페이지 크기는 1 이상이여야 합니다."))
    }

    @Test
    fun `파라미터 테스트_비정상_페이지넘버_MAX_케이스`() {
        val testSearch ="title"

        every {
            searchService.saveSearch(testSearch)
        } just Runs

        val paramMap: MultiValueMap<String, String> = LinkedMultiValueMap()
        paramMap.add("pageNumber", "51")
        paramMap.add("pageSize", "1")
        paramMap.add("sort", "recency")
        paramMap.add("query", testSearch)
        mockMvc.perform(
            get("/api/v1/search")
                .params(paramMap)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("페이지 번호는 50 이하이여야 합니다."))
    }

    @Test
    fun `파라미터 테스트_비정상_페이지넘버_MIN_케이스`() {
        val testSearch ="title"

        every {
            searchService.saveSearch(testSearch)
        } just Runs

        val paramMap: MultiValueMap<String, String> = LinkedMultiValueMap()
        paramMap.add("pageNumber", "51")
        paramMap.add("pageSize", "1")
        paramMap.add("sort", "recency")
        paramMap.add("query", testSearch)
        mockMvc.perform(
            get("/api/v1/search")
                .params(paramMap)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("페이지 번호는 50 이하이여야 합니다."))
    }

    @Test
    fun `파라미터 테스트_정상_NULLABLE_케이스`() {
        val testSearch ="title"

        every {
            searchService.saveSearch(testSearch)
        } just Runs

        val paramMap: MultiValueMap<String, String> = LinkedMultiValueMap()

        paramMap.add("query", testSearch)
        mockMvc.perform(
            get("/api/v1/search")
                .params(paramMap)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.data.meta.page_number").value("10"))
            .andExpect(jsonPath("$.code").value("200"))
    }

    @Test
    fun `인기검색어_테스트_정상`() {
        // given
        val testSearchList =arrayOf("title1","title","title1","title2","title3")
        val testlist = mutableListOf<RankResponseDto>()
        testSearchList.asList().stream().map {
            RankResponseDto(it,0, Collections.frequency(testSearchList.asList(), it))
        }.distinct().forEach {
            testlist.add(it)
        }
        testlist.sortedBy { it.count }

        every {
            searchService.findTopKeyword(10)
        } returns testlist

        mockMvc.perform(
            get("/api/v1/search/top/10")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.data[0].key").value(testlist.get(0).key))
            .andExpect(jsonPath("$.code").value("200"))
    }

    @Test
    fun `인기검색어_테스트_비정상_파라미터_오류`() {
        val testList = mutableListOf<RankResponseDto>()

        every {
            searchService.findTopKeyword(100)
        } returns testList


        mockMvc.perform(
            get("/api/v1/search/top/11")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("java.lang.IllegalArgumentException: rank 값은 반드시 1과 10 사이 값이여야 합니다."))
    }
}