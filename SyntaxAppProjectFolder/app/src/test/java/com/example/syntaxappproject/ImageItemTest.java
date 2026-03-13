package com.example.syntaxappproject;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ImageItemTest {
    @Test
    public void testEmptyConstructorCreatesObject() {
        ImageItem item = new ImageItem();
        assertNotNull(item);
    }
    @Test
    public void testConstructorSetsFieldsCorrectly() {
        ImageItem item = new ImageItem(
                "https://example.com/poster.jpg",
                "Yixing Li"
        );
        assertEquals("https://example.com/poster.jpg", item.imageUrl);
        assertEquals("Yixing Li", item.uploadedBy);
    }
}