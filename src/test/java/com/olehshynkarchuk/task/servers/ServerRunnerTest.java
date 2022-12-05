package com.olehshynkarchuk.task.servers;

import com.olehshynkarchuk.task.command.CommandContainer;
import com.olehshynkarchuk.task.servers.http.HttpServerFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServerRunnerTest {
    @Mock
    private HttpServerFactory httpServerFactory;
    @Mock
    private ServerRunner serverRunner;
    @Mock
    AbstractServer abstractServer;
    @InjectMocks
    CommandContainer commandFactory;

    void run() throws IOException {
        when(httpServerFactory.createServer(3000, commandFactory)).thenReturn(abstractServer);
        verify(serverRunner).isAlive();
    }
//
//    @Test
//    public void testClientSocketGetsCreated() throws IOException {
//        ServerSocket mockServerSocket = mock(ServerSocket.class);
//        when(mockServerSocket.accept()).thenReturn(new Socket());
//        assertNotNull(
//                SocketCreator.createClientSocket(mockServerSocket));
//    }
}