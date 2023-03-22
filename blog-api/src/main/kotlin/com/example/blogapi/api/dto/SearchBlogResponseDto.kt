package com.example.blogapi.api.dto

import com.example.kakaoclient.dto.KakaoBlogSearchResponseDto
import com.example.kakaoclient.dto.documents
import com.example.kakaoclient.dto.meta
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

data class SearchBlogResponseDto(
    @Schema(title = "메타데이터")
    val meta: MetaData,
    @Schema(title = "검색결과 리스트")
    val records: MutableList<Record>
) {
    companion object {
        fun of(kakaoBlogSearchResponseDto: KakaoBlogSearchResponseDto,pageNumber: Int,pageSize: Int) = SearchBlogResponseDto(
                MetaData.of(kakaoBlogSearchResponseDto.meta,pageSize,pageNumber),
            kakaoBlogSearchResponseDto.documents.map { Record.of(it) }.toMutableList()
        )

    }
}

data class Record(
    @Schema(title = "블로그 글 제목")
    val title: String,
    @Schema(title = "블로그 글 요약")
    val contents: String,
    @Schema(title = "블로그 글 URL")
    val url: String,
    @Schema(title = "블로그의 이름")
    val blogname: String,
    @Schema(title = "검색 시스템에서 추출한 대표 미리보기 이미지 URL, 미리보기 크기 및 화질은 변경될 수 있음")
    val thumbnail: String,
    @Schema(title = "블로그 글 작성시간, ISO 8601\n" +
            "[YYYY]-[MM]-[DD]T[hh]:[mm]:[ss].000+[tz]")
    val datetime: Date
){
    companion object {
        fun of(documents: documents)= Record(
                title = documents.title,
                contents = documents.contents,
                url = documents.url,
                blogname = documents.blogname,
                thumbnail = documents.thumbnail,
                datetime = documents.datetime
        )
    }
}

data class MetaData(
    @Schema(description = "검색된 문서 수")
    val total_count: Int,
    @Schema(description = "total_count 중 노출 가능 문서 수")
    val pageable_count: Int,
    @Schema(description = "현재 페이지가 마지막 페이지인지 여부, 값이 false면 page를 증가시켜 다음 페이지를 요청할 수 있음")
    val is_end: Boolean,
    @Schema(description = "페이지 사이즈")
    val page_size: Int,
    @Schema(description = "페이지 번호")
    val page_number: Int
){
    companion object {
        fun of(meta: meta,pageSize: Int,pageNumber: Int) = MetaData(
            total_count = meta.total_count,
            pageable_count = meta.pageable_count,
            page_size = pageSize,
            page_number = pageNumber,
            is_end = meta.is_end,
        )
    }
}
