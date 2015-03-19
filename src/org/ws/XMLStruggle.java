package org.ws;
import org.kxml2.kdom.Document;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * Created by IntelliJ IDEA.
 * User: rramele
 * Date: 02/02/12
 * Time: 10:03
 * To change this template use File | Settings | File Templates.
 */


public class XMLStruggle {

    private String namespace = "http://tempuri.org/";

    private String creationDate = "";
    private String id  = "";

        /**
     * Build the XML Element to be used within the header, for the Token Information.
     *
     * @return
     */
    private Element buildToken()
    {
        //<TokenInformation xmlns:i="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://www.w3.org/BaufestProductivityFramework">
        //<creationDate xmlns="http://schemas.datacontract.org/2004/07/Bpf.Security.Common">2011-12-20T17:39:59.1975044-03:00</creationDate>
        //<id xmlns:d4p1="http://www.w3.org/2001/XMLSchema" i:type="d4p1:string" xmlns="http://schemas.datacontract.org/2004/07/Bpf.Security.Common">f104d49d-1bfe-42e4-b355-e0fb94755bac</id>
        //</TokenInformation>

        String BPF_NAMESPACE = "http://www.w3.org/BaufestProductivityFramework";

        String BPF_SECURITY_NAMESPACE = "http://schemas.datacontract.org/2004/07/Bpf.Security.Common";

        Element tokenInformation = new Element().createElement(BPF_NAMESPACE, "TokenInformation");

        Element creationDate = new Element().createElement(BPF_SECURITY_NAMESPACE, "creationDate");
        creationDate.addChild(Node.TEXT, this.creationDate);
        tokenInformation.addChild(Node.ELEMENT, creationDate);

        Element id = new Element().createElement(BPF_SECURITY_NAMESPACE, "id");
        id.addChild(Node.TEXT, this.id);
        id.setAttribute("http://www.w3.org/2001/XMLSchema-instance", "type", "xsd:string");

        tokenInformation.addChild(Node.ELEMENT, id);

        tokenInformation.setPrefix("xsd", "http://www.w3.org/2001/XMLSchema");

        return tokenInformation;


    }


    private Element buildLoginSOAPRequest()
    {
        String BPF_SECURITY_NAMESPACE = "http://schemas.datacontract.org/2004/07/Bpf.Security.Common";

        Element root = new Element().createElement("http://schemas.xmlsoap.org/soap/envelope/", "Envelope");

        root.addChild(Node.ELEMENT,buildHeader());

        Element body = new Element().createElement("http://schemas.xmlsoap.org/soap/envelope/", "Body");

        root.addChild(Node.ELEMENT, body);


        //Element call = new Element().createElement("http://tempuri.org/", methodName);

        //body.addChild( Node.ELEMENT, call);


        // @FIXME, generalize and add parameters.


        // <LogInUserPass xmlns=\"http://tempuri.org/\">
        //<token xmlns:a=\"http://schemas.datacontract.org/2004/07/Bpf.Security.Common\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">" +
        //		"<a:company i:nil=\"true\" xmlns:b=\"http://schemas.datacontract.org/2004/07/Bpf.Security.Authentication.Common\" /><a:password>PortalProductores</a:password><a:userName>sancor</a:userName></token></LogInUserPass></s:Body></s:Envelope>";


        // ARREGLAR ACA


        Element logInUserName = new Element().createElement(namespace, "LogInUserPass");
        //logInUserName.setNamespace(namespace);

        body.addChild(Node.ELEMENT, logInUserName);

        Element token = new Element().createElement("", "token");
        token.setPrefix("i","http://www.w3.org/2001/XMLSchema-instance");
        token.setPrefix("d5p1",BPF_SECURITY_NAMESPACE);

        logInUserName.addChild(Node.ELEMENT, token);

        Element company = new Element().createElement(BPF_SECURITY_NAMESPACE, "company");
        company.setAttribute("","i:nil","true");
        company.setPrefix("b", "http://schemas.datacontract.org/2004/07/Bpf.Security.Authentication.Common");

        Element password = new Element().createElement(BPF_SECURITY_NAMESPACE, "password");
        password.addChild(Node.TEXT, "PortalProductores");

        Element realUsername = new Element().createElement(BPF_SECURITY_NAMESPACE,"userName");
        realUsername.addChild(Node.TEXT, "sancor");


        token.addChild(Node.ELEMENT, company);
        token.addChild(Node.ELEMENT, password);
        token.addChild(Node.ELEMENT, realUsername);

        return root;


    }

    private Element buildHeader()
    {
        String BPF_NAMESPACE = "http://www.w3.org/BaufestProductivityFramework";

        Element header = new Element().createElement("http://schemas.xmlsoap.org/soap/envelope/", "Header");

        header.addChild(Node.ELEMENT, buildAuthHeader());

        return header;
    }

    private String getLocalIpAddress()
    {
        return "192.168.10.10";
    }
        /**
     * Build the general authentication header.
     *
     * @return
     */
    private Element buildAuthHeader() {

        String BPF_NAMESPACE = "http://www.w3.org/BaufestProductivityFramework";

        String BPF_SERVICE_NAMESPACE = "http://schemas.datacontract.org/2004/07/Bpf.Common.Service";


        Element contextInformation = new Element().createElement(BPF_NAMESPACE, "ContextInformation");

        Element clientIp = new Element().createElement(BPF_SERVICE_NAMESPACE, "ClientIp");
        clientIp.addChild(Node.TEXT, getLocalIpAddress());
        contextInformation.addChild(Node.ELEMENT, clientIp);

        Element companyId = new Element().createElement(BPF_SERVICE_NAMESPACE, "CompanyId");
        companyId.addChild(Node.TEXT,"1");
        contextInformation.addChild(Node.ELEMENT, companyId);

        Element culture = new Element().createElement(BPF_SERVICE_NAMESPACE, "Culture");
        culture.addChild(Node.TEXT, "es-AR");
        contextInformation.addChild(Node.ELEMENT, culture);

        Element highCode = new Element().createElement(BPF_SERVICE_NAMESPACE, "HighCode");
        highCode.addChild(Node.TEXT, "0");
        contextInformation.addChild(Node.ELEMENT, highCode);

        Element maxId = new Element().createElement(BPF_SERVICE_NAMESPACE, "MaxId");
        maxId.addChild(Node.TEXT,"0");
        contextInformation.addChild(Node.ELEMENT, maxId);

        Element officeId = new Element().createElement(BPF_SERVICE_NAMESPACE, "OfficeId");
        officeId.addChild(Node.TEXT, "0");
        contextInformation.addChild(Node.ELEMENT, officeId);

        Element terminalId = new Element().createElement(BPF_SERVICE_NAMESPACE, "TerminalId");
        terminalId.addChild(Node.TEXT, "0");
        contextInformation.addChild(Node.ELEMENT, terminalId);

        Element timeZone = new Element().createElement(BPF_SERVICE_NAMESPACE, "TimeZone");
        //timeZone.addChild(Node.TEXT, "192.168.10.23");
        contextInformation.addChild(Node.ELEMENT, timeZone);

        Element userId = new Element().createElement(BPF_SERVICE_NAMESPACE, "UserId");
        userId.addChild(Node.TEXT, "0");
        contextInformation.addChild(Node.ELEMENT, userId);

        Element userName = new Element().createElement(BPF_SERVICE_NAMESPACE, "UserName");
        //userName.addChild(Node.TEXT, "192.168.10.23");
        contextInformation.addChild(Node.ELEMENT, userName);


        /**
        <ContextInformation xmlns="http://www.w3.org/BaufestProductivityFramework" xmlns:i="http://www.w3.org/2001/XMLSchema-instance">
        <ClientIp xmlns="http://schemas.datacontract.org/2004/07/Bpf.Common.Service">192.168.10.121</ClientIp>
        <CompanyId xmlns="http://schemas.datacontract.org/2004/07/Bpf.Common.Service">1</CompanyId>
        <Culture xmlns="http://schemas.datacontract.org/2004/07/Bpf.Common.Service">es-AR</Culture>
        <HighCode xmlns="http://schemas.datacontract.org/2004/07/Bpf.Common.Service">0</HighCode>
        <MaxId xmlns="http://schemas.datacontract.org/2004/07/Bpf.Common.Service">0</MaxId>
        <OfficeId xmlns="http://schemas.datacontract.org/2004/07/Bpf.Common.Service">0</OfficeId>
        <TerminalId xmlns="http://schemas.datacontract.org/2004/07/Bpf.Common.Service">0</TerminalId>
        <TimeZone i:nil="true" xmlns="http://schemas.datacontract.org/2004/07/Bpf.Common.Service" />
        <UserId xmlns="http://schemas.datacontract.org/2004/07/Bpf.Common.Service">0</UserId>
        <UserName xmlns="http://schemas.datacontract.org/2004/07/Bpf.Common.Service" />
        </ContextInformation>**/


        return contextInformation;
    }


private String serializeXMLElement(Element soapRequest) throws XmlPullParserException, IOException {

        XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
        parserFactory.setNamespaceAware(false);

        XmlSerializer serializer = parserFactory.newSerializer();

        ByteArrayOutputStream writer = new ByteArrayOutputStream();

        serializer.setOutput(writer, "UTF-8");
        serializer.startDocument("UTF-8", null);
        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", false);

        //serializer.setPrefix("", namespace);

        //serializer.startTag(namespace, "LogInUserPass");

        //serializer.endTag(namespace, "LogInUserPass");

        soapRequest.write(serializer);

        serializer.flush();

        System.out.println( "Element " + soapRequest.toString() );

        // @NOTE:  This is ugly, but I am afraid that checking in the documentation of KXmlSerializer (from kxml2) there is no way to omit the xml declaration from
        // the document.  SOAP Web Services do not like that declaration so we need to delete it explicitly by hand....

        String xmlWithoutHeader = writer.toString(); writer.writeTo(System.out);

        if (xmlWithoutHeader.startsWith("<?xml ")) {
            xmlWithoutHeader = xmlWithoutHeader.substring(xmlWithoutHeader.indexOf("?>") + 2);
        }

        return xmlWithoutHeader;
    }

    public static void main(String [] args) throws Exception
    {
            //Element soapRequest = new XMLStruggle().buildLoginSOAPRequest();

            //String xml  = new XMLStruggle().serializeXMLElement(soapRequest);

        //System.out.println( "xml:" + xml );

        Document doc = new Document();


        String xmlresponse = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\"><s:Header><ActivityId CorrelationId=\"00cef802-81c9-4318-9595-bf1526a7da44\" xmlns=\"http://schemas.microsoft.com/2004/09/ServiceModel/Diagnostics\">a4af4e36-8d88-480a-8aa3-85a73f17c03e</ActivityId></s:Header><s:Body><LogInUserPassResponse xmlns:jk=\"http://tempuri.org/\"><LogInUserPassResult xmlns:a=\"http://schemas.datacontract.org/2004/07/Bpf.Security.Common\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\"><a:creationDate>2012-02-02T14:01:49.6699744-03:00</a:creationDate><a:id i:type=\"b:string\" xmlns:b=\"http://www.w3.org/2001/XMLSchema\">55db7d0a-e221-47b9-99ef-008260e0ebdf</a:id></LogInUserPassResult></LogInUserPassResponse></s:Body></s:Envelope>";

        String xmlrfesponse = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\"><s:Header></s:Header></s:Envelope>";

        XmlPullParser parser;

        parser = XmlPullParserFactory.newInstance().newPullParser();


        ByteArrayInputStream data = new ByteArrayInputStream(xmlresponse.getBytes());
        parser.setInput(data, "UTF-8");

        doc.parse(parser);

        System.out.println( "XML Response:" + xmlresponse );
        System.out.println( "\nResponse:" +  new XMLStruggle().serializeXMLElement(doc.getRootElement())  );

    }

}
