/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.sharding.yaml.swapper.strategy;

import org.apache.shardingsphere.sharding.api.config.strategy.sharding.ComplexShardingStrategyConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.HintShardingStrategyConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.NoneShardingStrategyConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.sharding.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.sharding.yaml.config.strategy.sharding.YamlComplexShardingStrategyConfiguration;
import org.apache.shardingsphere.sharding.yaml.config.strategy.sharding.YamlHintShardingStrategyConfiguration;
import org.apache.shardingsphere.sharding.yaml.config.strategy.sharding.YamlNoneShardingStrategyConfiguration;
import org.apache.shardingsphere.sharding.yaml.config.strategy.sharding.YamlShardingStrategyConfiguration;
import org.apache.shardingsphere.sharding.yaml.config.strategy.sharding.YamlStandardShardingStrategyConfiguration;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class ShardingStrategyConfigurationYamlSwapperTest {
    
    private final ShardingStrategyConfigurationYamlSwapper shardingStrategyConfigurationYamlSwapper = new ShardingStrategyConfigurationYamlSwapper();
    
    @Test
    public void assertSwapToYamlWithStandard() {
        YamlShardingStrategyConfiguration actual = shardingStrategyConfigurationYamlSwapper.swap(new StandardShardingStrategyConfiguration("id", "standard"));
        assertThat(actual.getStandard().getShardingColumn(), is("id"));
        assertThat(actual.getStandard().getShardingAlgorithmName(), is("standard"));
        assertNull(actual.getComplex());
        assertNull(actual.getHint());
        assertNull(actual.getNone());
    }
    
    @Test
    public void assertSwapToYamlWithComplex() {
        YamlShardingStrategyConfiguration actual = shardingStrategyConfigurationYamlSwapper.swap(new ComplexShardingStrategyConfiguration("id, creation_date", "complex"));
        assertThat(actual.getComplex().getShardingColumns(), is("id, creation_date"));
        assertThat(actual.getComplex().getShardingAlgorithmName(), is("complex"));
        assertNull(actual.getStandard());
        assertNull(actual.getHint());
        assertNull(actual.getNone());
    }
    
    @Test
    public void assertSwapToYamlWithHint() {
        HintShardingAlgorithm hintShardingAlgorithm = mock(HintShardingAlgorithm.class);
        when(hintShardingAlgorithm.getType()).thenReturn("HINT_TEST");
        YamlShardingStrategyConfiguration actual = shardingStrategyConfigurationYamlSwapper.swap(new HintShardingStrategyConfiguration("hint"));
        assertThat(actual.getHint().getShardingAlgorithmName(), is("hint"));
        assertNull(actual.getStandard());
        assertNull(actual.getComplex());
        assertNull(actual.getNone());
    }
    
    @Test
    public void assertSwapToYamlWithNone() {
        YamlShardingStrategyConfiguration actual = shardingStrategyConfigurationYamlSwapper.swap(new NoneShardingStrategyConfiguration());
        assertNull(actual.getStandard());
        assertNull(actual.getComplex());
        assertNull(actual.getHint());
        assertThat(actual.getNone(), instanceOf(YamlNoneShardingStrategyConfiguration.class));
    }
    
    @Test
    public void assertSwapToObjectWithStandardWithRangeShardingAlgorithm() {
        StandardShardingStrategyConfiguration actual = (StandardShardingStrategyConfiguration) shardingStrategyConfigurationYamlSwapper.swap(createStandardShardingStrategyConfiguration());
        assertThat(actual.getShardingColumn(), is("id"));
        assertThat(actual.getShardingAlgorithmName(), is("standard"));
    }
    
    @Test
    public void assertSwapToObjectWithStandardWithoutRangeShardingAlgorithm() {
        StandardShardingStrategyConfiguration actual = (StandardShardingStrategyConfiguration) shardingStrategyConfigurationYamlSwapper.swap(createStandardShardingStrategyConfiguration());
        assertThat(actual.getShardingColumn(), is("id"));
        assertThat(actual.getShardingAlgorithmName(), is("standard"));
    }
    
    private YamlShardingStrategyConfiguration createStandardShardingStrategyConfiguration() {
        YamlStandardShardingStrategyConfiguration yamlStandardShardingStrategyConfiguration = new YamlStandardShardingStrategyConfiguration();
        yamlStandardShardingStrategyConfiguration.setShardingColumn("id");
        yamlStandardShardingStrategyConfiguration.setShardingAlgorithmName("standard");
        YamlShardingStrategyConfiguration result = new YamlShardingStrategyConfiguration();
        result.setStandard(yamlStandardShardingStrategyConfiguration);
        return result;
    }
    
    @Test
    public void assertSwapToObjectWithComplex() {
        ComplexShardingStrategyConfiguration actual = (ComplexShardingStrategyConfiguration) shardingStrategyConfigurationYamlSwapper.swap(createComplexShardingStrategyConfiguration());
        assertThat(actual.getShardingColumns(), is("id, creation_date"));
        assertThat(actual.getShardingAlgorithmName(), is("complex"));
    }
    
    private YamlShardingStrategyConfiguration createComplexShardingStrategyConfiguration() {
        YamlComplexShardingStrategyConfiguration yamlComplexShardingStrategyConfiguration = new YamlComplexShardingStrategyConfiguration();
        yamlComplexShardingStrategyConfiguration.setShardingColumns("id, creation_date");
        yamlComplexShardingStrategyConfiguration.setShardingAlgorithmName("complex");
        YamlShardingStrategyConfiguration result = new YamlShardingStrategyConfiguration();
        result.setComplex(yamlComplexShardingStrategyConfiguration);
        return result;
    }
    
    @Test
    public void assertSwapToObjectWithHint() {
        HintShardingStrategyConfiguration actual = (HintShardingStrategyConfiguration) shardingStrategyConfigurationYamlSwapper.swap(createHintShardingStrategyConfiguration());
        assertThat(actual.getShardingAlgorithmName(), is("hint"));
    }
    
    private YamlShardingStrategyConfiguration createHintShardingStrategyConfiguration() {
        YamlHintShardingStrategyConfiguration yamlHintShardingStrategyConfiguration = new YamlHintShardingStrategyConfiguration();
        yamlHintShardingStrategyConfiguration.setShardingAlgorithmName("hint");
        YamlShardingStrategyConfiguration result = new YamlShardingStrategyConfiguration();
        result.setHint(yamlHintShardingStrategyConfiguration);
        return result;
    }
    
    @Test
    public void assertSwapToObjectWithNone() {
        NoneShardingStrategyConfiguration actual = (NoneShardingStrategyConfiguration) shardingStrategyConfigurationYamlSwapper.swap(createNoneShardingStrategyConfiguration());
        assertThat(actual, instanceOf(NoneShardingStrategyConfiguration.class));
    }
    
    private YamlShardingStrategyConfiguration createNoneShardingStrategyConfiguration() {
        YamlNoneShardingStrategyConfiguration noneShardingStrategyConfig = new YamlNoneShardingStrategyConfiguration();
        YamlShardingStrategyConfiguration result = new YamlShardingStrategyConfiguration();
        result.setNone(noneShardingStrategyConfig);
        return result;
    }
    
    @Test
    public void assertSwapToObjectWithNull() {
        assertNull(shardingStrategyConfigurationYamlSwapper.swap(new YamlShardingStrategyConfiguration()));
    }
}
