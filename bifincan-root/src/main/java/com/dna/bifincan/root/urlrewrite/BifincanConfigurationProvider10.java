package com.dna.bifincan.root.urlrewrite;

import javax.servlet.ServletContext;
import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.config.Direction;
import org.ocpsoft.rewrite.servlet.config.DispatchType;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.Path;
import org.ocpsoft.rewrite.servlet.config.Redirect;

public class BifincanConfigurationProvider10 extends HttpConfigurationProvider {

    @Override
    public int priority() {
        return 10;
    }

    @Override
    public Configuration getConfiguration(final ServletContext context) {
        return ConfigurationBuilder.begin()
            .defineRule()
                .when(Direction.isInbound().and(DispatchType.isRequest())
                                           .and(Path.matches("{path}").where("path")
                                                                      .matches(".*"))
                     )
                .perform(Redirect.permanent("/fi{path}"));
        }

}
