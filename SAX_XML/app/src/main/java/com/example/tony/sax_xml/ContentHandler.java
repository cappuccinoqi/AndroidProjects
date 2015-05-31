package com.example.tony.sax_xml;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by Tony on 2015/5/31.
 */
public class ContentHandler extends DefaultHandler{
    private String nodeName;
    private StringBuilder versionCode;
    private StringBuilder versionName;
    private StringBuilder apkURL;

    @Override
    public void startDocument() throws SAXException {
        versionCode = new StringBuilder();
        versionName = new StringBuilder();
        apkURL = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        nodeName = localName;//记录当前节点名
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        //根据当前节点名判断将内容添加到哪一个stringBuilder对象中去
        if("versionCode".equals(nodeName)){
            versionCode.append(ch, start, length);
        }else if("apkURL".equals(nodeName)){
            apkURL.append(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if("updataInfo".equals(localName))
        {
            Log.d("ContentHanlder","versionCode is " + versionCode);
            Log.d("ContentHanlder","apkurl is " + apkURL);
            //清空StringBuilder
            versionCode.setLength(0);
            apkURL.setLength(0);
        }
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }
    public String getVersionCode(){
        return versionCode.toString();
    }
    public String getApkURL(){
        return apkURL.toString();
    }
}
