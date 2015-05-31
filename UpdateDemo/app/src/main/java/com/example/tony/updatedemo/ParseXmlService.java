package com.example.tony.updatedemo;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Tony on 2015/5/31.
 */
public class ParseXmlService {
    public HashMap<String ,String> parseXml(InputStream inStream) throws Exception{
        HashMap<String,String> hashMap = new HashMap<String,String>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();//实例化一个文档构建器工厂
        DocumentBuilder builder = factory.newDocumentBuilder();//通过文档构建器工厂获取一个文档构建器
        Document document = builder.parse(inStream);//通过文档构建器否见一个文档实例
        Element root  = document.getDocumentElement();//获取xml文件根节点
        NodeList childNodes = root.getChildNodes();//获得所有子节点
        for(int j = 0;j<childNodes.getLength();j++)
        {
            //遍历子节点
            Node childNode = (Node) childNodes.item(j);
            if(childNode.getNodeType() == Node.ELEMENT_NODE)
            {
                Element childElement = (Element) childNode;
                //版本号
                if("versionCode".equals(childElement.getNodeName()))
                {
                    hashMap.put("versionCode",childElement.getFirstChild().getNodeValue());
                }

            }
        }
        return hashMap;
    }
}
