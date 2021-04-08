package com.tiankong44.util;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.node.Image;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.AttributeProviderContext;
import org.commonmark.renderer.html.AttributeProviderFactory;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.*;

/**
 * @ClassName MarkdownUtil
 * @Description TODO
 * @Author 12481
 * @Date 18:16
 * @Version 1.0
 **/
public class MarkdownUtil {

    public static String markdownToHtml(String markdown) {
        //h标题生成id
        Set<Extension> headingAnchorExtensions = Collections.singleton(HeadingAnchorExtension.create());
        //转换table的HTML
        List<Extension> tableExtension = Arrays.asList(TablesExtension.create());
        Parser parser = Parser.builder()
                .extensions(tableExtension)
                .build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder()
                .extensions(headingAnchorExtensions)
                .extensions(tableExtension)
                .attributeProviderFactory(new AttributeProviderFactory() {
                    public AttributeProvider create(AttributeProviderContext context) {
                        return new CustomAttributeProvider();
                    }
                })
                .build();
        return renderer.render(document);

    }

    static class CustomAttributeProvider implements AttributeProvider {

        @Override
        public void setAttributes(Node node, String tagName, Map<String, String> attributes) {
            if (node instanceof Link) {
                attributes.put("target", "_blank");
            }
            if (node instanceof TableBlock) {
                attributes.put("class", "layui-table");
            }
            if (node instanceof Image) {
                attributes.put("v-image-preview","");
                attributes.put("style", "width:500px;height:350px;position:relative;");
                attributes.put("class", "blog-img");
            }

        }
    }

    public static void main(String[] args) {
        String str =
                "![](http://images.tiankong44.vip/1585308331112)\n" +
                "![](http://images.tiankong44.vip/1585308335563)\n";
        System.out.println(markdownToHtml(str));
    }
}
