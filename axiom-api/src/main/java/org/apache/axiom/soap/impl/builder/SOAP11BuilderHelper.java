/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.axiom.soap.impl.builder;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.exception.OMBuilderException;
import org.apache.axiom.soap.SOAP11Constants;
import org.apache.axiom.soap.SOAPFault;
import org.apache.axiom.soap.SOAPProcessingException;
import org.w3c.dom.Element;

import javax.xml.stream.XMLStreamReader;

public class SOAP11BuilderHelper extends SOAPBuilderHelper implements SOAP11Constants {
    private final SOAPFactoryEx factory;
    private boolean faultcodePresent = false;
    private boolean faultstringPresent = false;

    public SOAP11BuilderHelper(StAXSOAPModelBuilder builder, SOAPFactoryEx factory) {
        super(builder);
        this.factory = factory;
    }

    public OMElement handleEvent(XMLStreamReader parser,
                                 OMElement parent,
                                 int elementLevel) throws SOAPProcessingException {
        this.parser = parser;

        OMElement element = null;
        String localName = parser.getLocalName();

        if (elementLevel == 4) {

            if (SOAP_FAULT_CODE_LOCAL_NAME.equals(localName)) {

                element = factory.createSOAPFaultCode(
                        (SOAPFault) parent, builder);
                faultcodePresent = true;
            } else if (SOAP_FAULT_STRING_LOCAL_NAME.equals(localName)) {

                element = factory.createSOAPFaultReason(
                        (SOAPFault) parent, builder);
                faultstringPresent = true;
            } else if (SOAP_FAULT_ACTOR_LOCAL_NAME.equals(localName)) {
                element =
                        factory.createSOAPFaultRole((SOAPFault) parent,
                                                    builder);
            } else if (SOAP_FAULT_DETAIL_LOCAL_NAME.equals(localName)) {
                element =
                        factory.createSOAPFaultDetail((SOAPFault) parent,
                                                      builder);
            } else {
                element =
                        factory.createOMElement(
                                localName, parent, builder);
            }

        } else if (elementLevel == 5) {

            String parentTagName = "";
            if (parent instanceof Element) {
                parentTagName = ((Element) parent).getTagName();
            } else {
                parentTagName = parent.getLocalName();
            }

            if (parentTagName.equals(SOAP_FAULT_CODE_LOCAL_NAME)) {
                throw new SOAPProcessingException(
                        "faultcode element should not have children");
            } else if (parentTagName.equals(
                    SOAP_FAULT_STRING_LOCAL_NAME)) {
                throw new SOAPProcessingException(
                        "faultstring element should not have children");
            } else if (parentTagName.equals(
                    SOAP_FAULT_ACTOR_LOCAL_NAME)) {
                throw new SOAPProcessingException(
                        "faultactor element should not have children");
            } else {
                element =
                        this.factory.createOMElement(
                                localName, parent, builder);
            }

        } else if (elementLevel > 5) {
            element =
                    this.factory.createOMElement(localName,
                                                 parent,
                                                 builder);
        }

        return element;
    }
}