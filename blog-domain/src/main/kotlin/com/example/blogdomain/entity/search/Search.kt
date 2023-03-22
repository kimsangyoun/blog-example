package com.example.blogdomain.entity.search

import com.example.blogdomain.entity.BaseTimeEntity
import javax.persistence.*

@Table(name = "Search")
@Entity
class Search(
    @Column(name = "SEARCH_CONTENTS")
    var contents: String,
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEARCH_SEQUENCE")
    @Id
    var id: Long = 0L
): BaseTimeEntity()