package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.models.Todo;
import com.example.demo.services.TodoNotFoundException;
import com.example.demo.services.TodoService;
import com.google.common.collect.Lists;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.headers.ResponseHeadersSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * ApiDocumentation
 */
@SpringBootTest
@ExtendWith({ RestDocumentationExtension.class, SpringExtension.class })
public class TodoApplicationTests {

    MockMvc mockMvc;
 
    RestDocumentationContextProvider restDocumentation;

    @MockBean
    TodoService service;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)
                .operationPreprocessors().withResponseDefaults(prettyPrint()))
                .alwaysDo(print())
                .alwaysDo(document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .build();
    }

    @Test
    public void findAllShouldReturnListOfTodos() throws Exception {
        when(service.findAll(any(Pageable.class))).thenReturn(
                new PageImpl<>(Lists.newArrayList(
                    new Todo(1L, "Do it", true), 
                    new Todo(2L, "Do it again", false))
            )
        );
        mockMvc.perform(get("/api/v1/todos?page=0&size=2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.[0].title", is("Do it")))
                .andExpect(jsonPath("$.[0].completed", is(true))).andExpect(jsonPath("$.[1].title", is("Do it again")))
                .andExpect(jsonPath("$.[1].completed", is(false))).andExpect(header().longValue("X-Todos-Total", 2L))
                .andDo(document("{method-name}", pageParameters(), todoCollection(), pageHeaders()));
        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(service).findAll(captor.capture());
        assertThat(captor.getValue().getPageNumber()).isEqualTo(0);
        assertThat(captor.getValue().getPageSize()).isEqualTo(2);
    }

    @Test
    public void findOneShouldReturnTodo() throws Exception {
        when(service.findById(1L)).thenReturn(new Todo(1L, "Do it", false));
        mockMvc.perform(get("/api/v1/todos/{id}", 1L).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Do it")))
                .andExpect(jsonPath("$.completed", is(false)))
                .andDo(document("{method-name}",
                    pathParameters(parameterWithName("id").description("The unique identifier of the todo")), todo())
                );
    }

    @Test
    public void findOneShouldReturnErrorIfNotFound() throws Exception {
        when(service.findById(-1L)).thenThrow(new TodoNotFoundException(-1L));
        mockMvc.perform(get("/api/v1/todos/{id}", -1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is("NOT_FOUND")))
                .andExpect(jsonPath("$.message", is("Could not find a todo with id -1")))
                .andDo(document("{method-name}",
                        pathParameters(parameterWithName("id").description("The unique identifier of the user")), apiError())
                );
    }

    private ResponseFieldsSnippet apiError() {
        return responseFields(fieldWithPath("status").description("The HTTP status of the request"),
                fieldWithPath("timestamp").description("The timestamp of the error"),
                fieldWithPath("path").description("The path of the requested resource"),
                fieldWithPath("message").description("The error message"),
                fieldWithPath("debug").description("A detailed description of the error").type(STRING).optional(),
                fieldWithPath("errors").description("The list of errors if any").type(ARRAY).optional());
    }

    private ResponseFieldsSnippet todo() {
        return responseFields(
            fieldWithPath("title").description("The title of the todo"),
            fieldWithPath("completed").description("The completion status of the todo").optional());
    }

    private RequestParametersSnippet pageParameters() {
        return requestParameters(
            parameterWithName("page").description("The page to retrieve").optional(),
            parameterWithName("size").description("The number of elements within a single page").optional()
        );
    }

    private ResponseFieldsSnippet todoCollection() {
        return responseFields(
            fieldWithPath("[].title").description("The title of the todo"),
            fieldWithPath("[].completed").description("The completion status of the todo").optional());
    }

    private ResponseHeadersSnippet pageHeaders() {
        return responseHeaders(headerWithName("X-Todos-Total").description("The total amount of todos"));
    }
    
}