package com.olehshynkarchuk.task.command;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.olehshynkarchuk.task.goods.GoodsRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.olehshynkarchuk.task.command.CommandContainer.Command.getCommand;
import static org.junit.jupiter.api.Assertions.*;

class CommandContainerTest {
    static CommandContainer container;

    @BeforeAll
    static void beforeAll() {
        container = new CommandContainer(new GoodsRepository(), new JsonMapper());
    }

    @Test
    void testCommandContainerNotEmpty() {
        assertFalse(container.commandList.isEmpty());
    }

    @Test
    void testCommandContainerGetCommand() {
        assertEquals(SearchSingleGoodsCommand.class,
                container.commandList.get(getCommand("GET", "/shop/item/12/get")).getClass());
        assertEquals(GoodsSizeCommand.class,
                container.commandList.get(getCommand("GET", "get count")).getClass());
        assertNotEquals(SearchSingleGoodsCommand.class,
                container.commandList.get(getCommand("GET", "get item=-1")).getClass());
        assertNotEquals(SearchSingleGoodsCommand.class,
                container.commandList.get(getCommand("GET", "/shop/item/A/get")).getClass());
        assertEquals(GoodsAllCommand.class,
                container.commandList.get(getCommand("GET", "/shop/items")).getClass());
        assertNotEquals(GoodsAllCommand.class,
                container.commandList.get(getCommand("POST", "get items")).getClass());
    }

    @Test
    void testCommandContainerOtherCommand() {
        assertEquals(NewGoodsCommand.class,
                container.commandList.get(getCommand("POST", "/shop/item/new")).getClass());
        assertEquals(NewGoodsCommand.class,
                container.commandList.get(getCommand("POST", "new item")).getClass());
        assertNotEquals(NewGoodsCommand.class,
                container.commandList.get(getCommand("POST", "/shop/item/1/new")).getClass());
        assertNotEquals(NewGoodsCommand.class,
                container.commandList.get(getCommand("PUT", "/shop/item/new")).getClass());

        assertEquals(ReplaceGoodsCommand.class,
                container.commandList.get(getCommand("PUT", "/shop/item/111/put")).getClass());
        assertNotEquals(ReplaceGoodsCommand.class,
                container.commandList.get(getCommand("PUT", "put itemS=1")).getClass());
        assertEquals(ReplaceGoodsCommand.class,
                container.commandList.get(getCommand("PUT", "put item=1")).getClass());
        assertNotEquals(ReplaceGoodsCommand.class,
                container.commandList.get(getCommand("GET", "put item=1")).getClass());

        assertEquals(DeleteGoodsCommand.class,
                container.commandList.get(getCommand("DELETE", "delete item=2")).getClass());
        assertNotEquals(DeleteGoodsCommand.class,
                container.commandList.get(getCommand("DELETE", "/shop/item/A/delete")).getClass());
        assertEquals(DeleteGoodsCommand.class,
                container.commandList.get(getCommand("DELETE", "/shop/item/1/delete")).getClass());
        assertNotEquals(DeleteGoodsCommand.class,
                container.commandList.get(getCommand("PUT", "/shop/item/1/delete")).getClass());

    }

    @Test
    void testCommandContainerUnknownCommand() {
        assertEquals(UnknownCommand.class,
                container.commandList.get(getCommand("asdasdsa", "asd")).getClass());
        assertEquals(UnknownCommand.class,
                container.commandList.get(getCommand("PATCH", "put item=1")).getClass());
    }
}