/**
 * Copyright (C) 2007 - 2015 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *       • Apache License, version 2.0
 *       • Apache Software License, version 1.0
 *       • GNU Lesser General Public License, version 3
 *       • Mozilla Public License, versions 1.0, 1.1 and 2.0
 *       • Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * As an exception to the terms of the GPL, you may copy, modify,
 * propagate, and distribute a work formed by combining 52°North WPS
 * GeoTools Modules with the Eclipse Libraries, or a work derivative of
 * such a combination, even if such copying, modification, propagation, or
 * distribution would otherwise violate the terms of the GPL. Nothing in
 * this exception exempts you from complying with the GPL in all respects
 * for all of the code used other than the Eclipse Libraries. You may
 * include this exception and its grant of permissions when you distribute
 * 52°North WPS GeoTools Modules. Inclusion of this notice with such a
 * distribution constitutes a grant of such permissions. If you do not wish
 * to grant these permissions, remove this paragraph from your
 * distribution. "52°North WPS GeoTools Modules" means the 52°North WPS
 * modules using GeoTools functionality - software licensed under version 2
 * or any later version of the GPL, or a work based on such software and
 * licensed under the GPL. "Eclipse Libraries" means Eclipse Modeling
 * Framework Project and XML Schema Definition software distributed by the
 * Eclipse Foundation and licensed under the Eclipse Public License Version
 * 1.0 ("EPL"), or a work based on such software and licensed under the EPL.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.wps.io.test.datahandler.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.junit.Assert;
import org.junit.Test;
import org.n52.wps.io.data.binding.complex.AsciiGrassDataBinding;
import org.n52.wps.io.data.binding.complex.GTRasterDataBinding;
import org.n52.wps.io.datahandler.generator.AsciiGrassGenerator;
import org.n52.wps.io.datahandler.parser.AsciiGrassParser;
import org.n52.wps.io.datahandler.parser.GeotiffParser;
import org.n52.wps.io.test.datahandler.AbstractTestCase;

public class AsciiGrassGeneratorTest extends AbstractTestCase<AsciiGrassGenerator> {

    @Test
    public void testGenerator() {

        if(!isDataHandlerActive()){
            return;
        }

        String testFilePath = projectRoot
                + "/52n-wps-io-geotools/src/test/resources/6_UTM2GTIF.TIF";

        try {
            testFilePath = URLDecoder.decode(testFilePath, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            Assert.fail(e1.getMessage());
        }

        GeotiffParser theParser = new GeotiffParser();

        String[] mimetypes = theParser.getSupportedFormats();

        InputStream input = null;

        try {
            input = new FileInputStream(new File(testFilePath));
        } catch (FileNotFoundException e) {
            Assert.fail(e.getMessage());
        }

        GTRasterDataBinding theBinding = theParser.parse(input, mimetypes[0],
                null);

        Assert.assertTrue(theBinding.getPayload() != null);

        String[] mimetypes2 = dataHandler.getSupportedFormats();

        AsciiGrassParser asciiGrassParser = new AsciiGrassParser();

        for (String string : mimetypes2) {
            try {
                InputStream resultStream = dataHandler.generateStream(theBinding, string, null);

                AsciiGrassDataBinding rasterBinding = asciiGrassParser.parse(resultStream, mimetypes[0], null);

                Assert.assertTrue(rasterBinding.getPayload() != null);
                Assert.assertTrue(rasterBinding.getPayload().getDimension() != 0);
                Assert.assertTrue(rasterBinding.getPayload().getEnvelope() != null);

                InputStream resultStreamBase64 = dataHandler.generateBase64Stream(theBinding, string, null);

                AsciiGrassDataBinding rasterBindingBase64 = (AsciiGrassDataBinding) asciiGrassParser.parseBase64(resultStreamBase64, mimetypes[0], null);

                Assert.assertTrue(rasterBindingBase64.getPayload() != null);
                Assert.assertTrue(rasterBindingBase64.getPayload().getDimension() != 0);
                Assert.assertTrue(rasterBindingBase64.getPayload().getEnvelope() != null);

            } catch (IOException e) {
                e.printStackTrace();
                Assert.fail(e.getMessage());
            }

        }

    }

    @Override
    protected void initializeDataHandler() {
        dataHandler = new AsciiGrassGenerator();

    }


}
