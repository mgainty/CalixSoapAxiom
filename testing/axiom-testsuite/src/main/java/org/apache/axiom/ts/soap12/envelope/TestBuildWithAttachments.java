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
package org.apache.axiom.ts.soap12.envelope;

import java.io.InputStream;
import java.util.Iterator;

import javax.activation.DataHandler;

import org.apache.axiom.attachments.Attachments;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMMetaFactory;
import org.apache.axiom.om.OMText;
import org.apache.axiom.om.OMXMLBuilderFactory;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.testutils.io.IOTestUtils;
import org.apache.axiom.ts.AxiomTestCase;
import org.apache.axiom.ts.soap.MTOMSample;

public class TestBuildWithAttachments extends AxiomTestCase {
    public TestBuildWithAttachments(OMMetaFactory metaFactory) {
        super(metaFactory);
    }

    protected void runTest() throws Throwable {
        MTOMSample sample = MTOMSample.SAMPLE1;
        InputStream in = sample.getInputStream();
        Attachments attachments = new Attachments(in, sample.getContentType());
        SOAPEnvelope envelope = OMXMLBuilderFactory.createSOAPModelBuilder(metaFactory, attachments).getSOAPEnvelope();
        envelope.buildWithAttachments();
        in.close();
        Iterator it = envelope.getBody().getFirstElement().getChildElements();
        OMElement image1 = (OMElement)it.next();
        OMElement image2 = (OMElement)it.next();
        
        IOTestUtils.compareStreams(((DataHandler)((OMText)image1.getFirstOMChild()).getDataHandler()).getInputStream(),
                sample.getPart(1));

        IOTestUtils.compareStreams(((DataHandler)((OMText)image2.getFirstOMChild()).getDataHandler()).getInputStream(),
                sample.getPart(2));
    }
}