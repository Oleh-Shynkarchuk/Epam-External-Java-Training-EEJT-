package com.olehshynkarchuk.task.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.olehshynkarchuk.task.constant.Constants.HttpStatusCodeResponse.OK_CODE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SocketIOTest {

    @Mock
    Socket clientSocket;
    @Mock
    InputStream inputStream;
    @Mock
    OutputStream outputStream;
    @InjectMocks
    SocketIO socketIO;

    @Test
    void shouldReturnSocketIO() throws IOException {

        when(clientSocket.getInputStream()).thenReturn(inputStream);
        when(clientSocket.getOutputStream()).thenReturn(outputStream);

        assertNotNull(socketIO.createSocketReader());
        assertNotNull(socketIO.createSocketWriter());
    }

    @Test
    void shouldReturnReadLine() {

        String inputString = "Hello\n";
        BufferedReader input = new BufferedReader(new InputStreamReader(
                new ByteArrayInputStream(inputString.getBytes())));

        assertEquals("Hello", socketIO.readLine(input));
    }

    @Test
    void sendTcpResponds() {
        //given
        Map<Integer, String> message = Map.of(OK_CODE, "testMessage");
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintWriter printWriter = new PrintWriter(
                outContent, true);
        String expected = "<" + message.entrySet().iterator().next().getValue() + ">\r\n";

        //when
        socketIO.sendTcpResponds(printWriter, message);

        //then
        assertEquals(expected, outContent.toString());

    }

    @Test
    void shouldReturnLines() {
        String actual = "bla";
        Stream<String> expected = Stream.of("b,l,a".split(","));
        BufferedReader input = new BufferedReader(new InputStreamReader(
                new ByteArrayInputStream(actual.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8));

        assertEquals(expected.collect(Collectors.joining()), socketIO.lines(input).collect(Collectors.joining()));
    }

    @Test
    void shouldReturnContentLength() {
        char[] bodyChar = {'r', 's', 't', 'u', 'v'};
        int expected = 5;
        BufferedReader input = new BufferedReader(new InputStreamReader(
                new ByteArrayInputStream(Arrays.toString(bodyChar).getBytes(StandardCharsets.UTF_8))));


        assertEquals(expected, socketIO.read(input, bodyChar, 0, bodyChar.length));
    }

    @Test
    void sendHttpResponds() {
        //given
        Map<Integer, String> message = Map.of(OK_CODE, "testMessage");
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintWriter printWriter = new PrintWriter(
                outContent, true);
        String httpVersion = "HTTP/1.1";
        String expected = httpVersion + " " + message.entrySet().iterator().next().getKey() + "\r\n" +
                "Content-Type: application/json; charset=utf-8\r\n" +
                "\r\n" +
                message.entrySet().iterator().next().getValue() + "\r\n";
        //when
        socketIO.sendHttpResponds(printWriter, httpVersion, message);

        //then
        assertEquals(expected, outContent.toString());
    }

    @Test
    void shouldCloseIO() throws IOException {

        when(clientSocket.getInputStream()).thenReturn(inputStream);
        when(clientSocket.getOutputStream()).thenReturn(outputStream);
        socketIO.createSocketReader();
        socketIO.createSocketWriter();
        assertDoesNotThrow(() -> socketIO.close());

    }
}