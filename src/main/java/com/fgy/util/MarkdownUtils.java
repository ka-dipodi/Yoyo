package com.fgy.util;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.AttributeProviderContext;
import org.commonmark.renderer.html.AttributeProviderFactory;
import org.commonmark.renderer.html.HtmlRenderer;


import java.util.*;

/**
 * Markdown转换为HTML的工具类
* */
public class MarkdownUtils {


    /**
     * Markdown转换为HTML格式
     * */
    public static String markdownToHTML(String markdown){
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        renderer.render(document);  // "<p>This is <em>Sparta</em></p>\n"
        return renderer.render(document);
    }

    /**
     *Markdown转换为HTML格式
     *添加描点生成(生成目录)，表格生成
     */
    public static String markdownToHTMLExtensions(String markdown){
        //h标题生成id
        Set<Extension> headingAnchorExtensions = Collections.singleton(HeadingAnchorExtension.create());
        //转换table的HTML
        List<Extension> tableException = Arrays.asList(TablesExtension.create());
        Parser parser= Parser.builder()
                .extensions(tableException)
                .build();
        Node document=parser.parse(markdown);
        HtmlRenderer renderer= HtmlRenderer.builder()
                .extensions(headingAnchorExtensions)
                .extensions(tableException)
                .attributeProviderFactory(new AttributeProviderFactory() {
                    @Override
                    public AttributeProvider create(AttributeProviderContext attributeProviderContext) {
                        return new CustomAttributeProvide();
                    }
                }).build();
            return renderer.render(document);
    }
    /**
     *处理a table标签的属性
     */
    static class CustomAttributeProvide implements AttributeProvider {

        @Override
        public void setAttributes(Node node, String s, Map<String, String> map) {
            //改变a标签的target属性为_blank
            if(node instanceof Link){
                map.put("target","_blank");
            }
            //改变table标签为Semantic-ui的样式
            if (node instanceof TableBlock){
                map.put("class","ui celled table");
            }

        }
    }



}
