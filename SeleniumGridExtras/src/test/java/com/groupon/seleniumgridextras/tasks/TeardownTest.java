/**
 * Copyright (c) 2013, Groupon, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * Neither the name of GROUPON nor the names of its contributors may be
 * used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 * Created with IntelliJ IDEA.
 * User: Dima Kovalenko (@dimacus) && Darko Marinov
 * Date: 5/10/13
 * Time: 4:06 PM
 */


package com.groupon.seleniumgridextras.tasks;

import com.groupon.seleniumgridextras.JsonWrapper;
import com.groupon.seleniumgridextras.RuntimeConfig;
import com.groupon.seleniumgridextras.WriteDefaultConfigs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import static org.junit.Assert.assertEquals;


public class TeardownTest {

  public ExecuteOSTask task;

  @Before
  public void setUp() throws Exception {
    RuntimeConfig.setConfig("teardown_test.json");
    WriteDefaultConfigs.writeConfig(RuntimeConfig.getConfigFile(), false);
    RuntimeConfig.loadConfig();

    task = new Teardown();
    Boolean initilized = task.initialize();
  }

  @After
  public void tearDown() throws Exception {
    File config = new File(RuntimeConfig.getConfigFile());
    config.delete();
  }

  @Test
  public void testExecute() {
    if (!java.awt.GraphicsEnvironment.isHeadless()) {
      String result = task.execute();
      Map<String, HashMap> parsedResult = JsonWrapper.parseJson(result);
      Long exitCode = new Long(0);
      List<String> expecteClasses = new LinkedList<String>();
      expecteClasses.add("KillAllIE");
      expecteClasses.add("MoveMouse");

      assertEquals(exitCode, parsedResult.get("exit_code"));
      assertEquals(expecteClasses, parsedResult.get("classes_to_execute"));
    }
  }

  @Test
  public void testGetEndpoint() throws Exception {
    assertEquals("/teardown", task.getEndpoint());
  }

  @Test
  public void testGetDescription() throws Exception {
    assertEquals("Calls several pre-defined tasks to act as teardown after build",
                 task.getDescription());
  }

  @Test
  public void testGetJsonResponse() throws Exception {
    if (!java.awt.GraphicsEnvironment.isHeadless()) {

      assertEquals(
          "{\"exit_code\":0,\"results\":[\"\"],\"error\":[],\"classes_to_execute\":[\"KillAllIE\",\"MoveMouse\"],\"out\":[]}",
          task.getJsonResponse().toString());

      assertEquals("List of full canonical classes to execute on Tear-Down",
                   task.getJsonResponse().getKeyDescriptions().get("classes_to_execute"));

      assertEquals("Hash object of tasks ran and their results",
                   task.getJsonResponse().getKeyDescriptions().get("results"));

      assertEquals(5, task.getResponseDescription().keySet().size());
    }
  }

  @Test
  public void testGetAcceptedParams() throws Exception {
    assertEquals(0, task.getAcceptedParams().keySet().size());
  }


}
