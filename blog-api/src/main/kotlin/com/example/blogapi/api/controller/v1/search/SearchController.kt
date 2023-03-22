package com.example.blogapi.api.controller.v1.search

import com.example.blogapi.api.dto.BlogSearchRequestDto
import com.example.blogapi.api.dto.RankResponseDto
import com.example.blogapi.api.dto.SearchBlogResponseDto
import com.example.blogapi.api.service.SearchService
import com.example.blogapi.common.ResponseService
import com.example.blogapi.common.dto.ListResult
import com.example.blogapi.common.dto.SingleResult
import com.example.kakaoclient.dto.KakaoBlogSearchRequestDto
import com.example.kakaoclient.service.KakaoBlogService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Tag(name = "SEARCH", description = "검색 API Document")
@RestController
@RequestMapping("/api/v1/search")
class SearchController(
    private val kakaoBlogService: KakaoBlogService,
    private val searchService: SearchService,
    private val responseService: ResponseService
) {
    @Operation(summary = "블로그 검색 API")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Success"),
            ApiResponse(responseCode = "400", description = "Illigal request"),
            ApiResponse(responseCode = "500", description = "Server Error")]
    )
    @Parameters(value = [
        Parameter(name = "query", description = "검색키워드", required = true, `in` = ParameterIn.QUERY),
        Parameter(name = "pageNumber", description = "요청 페이지(디폴트 10)", required = false, `in` = ParameterIn.QUERY ),
        Parameter(name = "sort", description = "정렬(디폴트 accuracy)", required = false, `in` = ParameterIn.QUERY, example = "accuracy, recency" ),
        Parameter(name = "pageSize", description = "페이지 요청 크기(디폴트 10)", required = false, `in` = ParameterIn.QUERY),
        Parameter(name = "blogSearchRequestDto", hidden = true)
    ])
    @GetMapping
    @ResponseBody
    fun search(
        @Valid blogSearchRequestDto: BlogSearchRequestDto
    ): SingleResult<SearchBlogResponseDto> {
        kakaoBlogService.searchBlogByKakaoClient(
            KakaoBlogSearchRequestDto(
                query = blogSearchRequestDto.query!!,
                size = blogSearchRequestDto.pageSize,
                sort = blogSearchRequestDto.sort,
                page = blogSearchRequestDto.pageNumber
            )
        )?.let { SearchBlogResponseDto.of(it, pageNumber = blogSearchRequestDto.pageNumber!!, pageSize = blogSearchRequestDto.pageSize!!) }.run {
            searchService.saveSearch(blogSearchRequestDto.query)
            return responseService.getSingleResult(this!!)
        }
    }

    @Operation(summary = "인기 검색어 조회 API", description = "요청한 rank 갯수 만큼의 인기검색어를 반환한다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Success"),
            ApiResponse(responseCode = "400", description = "Illigal request"),
            ApiResponse(responseCode = "500", description = "Server Error")]
    )
    @Parameters(value = [
        Parameter(name = "rank", description = "랭크", required = true, `in` = ParameterIn.PATH)
    ])
    @GetMapping("/top/{rank}")
    @ResponseBody
    fun findRank(@PathVariable rank: Int): ListResult<RankResponseDto> {
        if (rank < 1 || rank > 10) {
            //todo 메시지 프로퍼티 화 및 validator 처리 필요
            throw IllegalArgumentException("rank 값은 반드시 1과 10 사이 값이여야 합니다.")
        }
        return responseService.getListResult(searchService.findTopKeyword(rank))
    }
}