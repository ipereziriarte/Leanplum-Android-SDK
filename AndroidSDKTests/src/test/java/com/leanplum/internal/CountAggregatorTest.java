/*
 * Copyright 2018, Leanplum, Inc. All rights reserved.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.leanplum.internal;

import com.leanplum.__setup.AbstractTest;

import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * @author Grace Gu
 */
public class CountAggregatorTest extends AbstractTest {
  @Test
  public void testIncrementDisabledCount() {
    CountAggregator countAggregator = new CountAggregator();
    String testString = "test";

    countAggregator.incrementCount(testString);
    HashMap<String, Integer> counts = countAggregator.getCounts();
    assertEquals(null, counts.get(testString));

    countAggregator.incrementCount(testString);
    counts = countAggregator.getCounts();
    assertEquals(null, counts.get(testString));
  }

  @Test
  public void testIncrementCount() {
    CountAggregator countAggregator = new CountAggregator();
    String testString = "test";
    HashSet<String> testSet = new HashSet<String>(Arrays.asList(testString));
    countAggregator.setEnabledCounters(testSet);

    countAggregator.incrementCount(testString);
    HashMap<String, Integer> counts = countAggregator.getCounts();
    assertEquals(1, counts.get(testString).intValue());

    countAggregator.incrementCount(testString);
    counts = countAggregator.getCounts();
    assertEquals(2, counts.get(testString).intValue());
  }

  @Test
  public void testIncrementDisabledCountMultiple() {
    CountAggregator countAggregator = new CountAggregator();
    String testString = "test";

    countAggregator.incrementCount(testString, 2);
    HashMap<String, Integer> counts = countAggregator.getCounts();
    assertEquals(null, counts.get(testString));

    countAggregator.incrementCount(testString, 15);
    counts = countAggregator.getCounts();
    assertEquals(null, counts.get(testString));
  }

  @Test
  public void testIncrementCountMultiple() {
    CountAggregator countAggregator = new CountAggregator();
    String testString = "test";
    HashSet<String> testSet = new HashSet<String>(Arrays.asList(testString));
    countAggregator.setEnabledCounters(testSet);

    countAggregator.incrementCount(testString, 2);
    HashMap<String, Integer> counts = countAggregator.getCounts();
    assertEquals(2, counts.get(testString).intValue());

    countAggregator.incrementCount(testString, 15);
    counts = countAggregator.getCounts();
    assertEquals(17, counts.get(testString).intValue());
  }

  @Test
  public void testGetAndClearCounts() {
    CountAggregator countAggregator = new CountAggregator();
    String testString = "test";
    String testString2 = "test2";
    HashSet<String> testSet = new HashSet<String>(Arrays.asList(testString, testString2));
    countAggregator.setEnabledCounters(testSet);

    countAggregator.incrementCount(testString, 2);
    countAggregator.incrementCount(testString2, 15);

    HashMap<String, Integer> previousCounts = countAggregator.getAndClearCounts();
    HashMap<String, Integer> counts = countAggregator.getCounts();

    //check counts is empty after clearing
    assertEquals(true, counts.isEmpty());
    //test counts transferred to previousCounts
    assertEquals(2, previousCounts.get(testString).intValue());
    assertEquals(15, previousCounts.get(testString2).intValue());
  }

  @Test
  public void testMakeParams() {
    CountAggregator countAggregator = new CountAggregator();
    String testString = "test";
    HashMap<String, Object> params = countAggregator.makeParams(testString, 2);

    assertEquals(Constants.Values.SDK_COUNT, params.get(Constants.Params.TYPE));
    assertEquals(testString, params.get(Constants.Params.MESSAGE));
    assertEquals(2, params.get(Constants.Params.COUNT));
  }
}
