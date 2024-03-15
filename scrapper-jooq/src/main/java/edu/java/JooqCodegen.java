package edu.java;

import org.jooq.SQLDialect;
import org.jooq.codegen.GenerationTool;
import org.jooq.impl.DSL;
import org.jooq.meta.jaxb.Configuration;
import org.jooq.meta.jaxb.Database;
import org.jooq.meta.jaxb.Generate;
import org.jooq.meta.jaxb.Generator;
import org.jooq.meta.jaxb.Jdbc;
import org.jooq.meta.jaxb.Property;
import org.jooq.meta.jaxb.Target;

@SuppressWarnings("checkstyle:UncommentedMain")
public final class JooqCodegen {
    private JooqCodegen() {
    }

    @SuppressWarnings("checkstyle:MultipleStringLiterals")
    public static void main(String[] args) throws Exception {
        Database database = new Database()
            //.withName("org.jooq.meta.extensions.liquibase.LiquibaseDatabase")
            .withName("org.jooq.meta.postgres.PostgresDatabase")
            .withProperties(
                new Property().withKey("rootPath").withValue("migrations"),
                new Property().withKey("scripts").withValue("master.xml")
            )
            .withIncludes(".*")
            .withExcludes("")
            .withInputSchema("public");

        Generate options = new Generate()
            .withGeneratedAnnotation(true)
            .withGeneratedAnnotationDate(false)
            .withNullableAnnotation(true)
            .withNullableAnnotationType("org.jetbrains.annotations.Nullable")
            .withNonnullAnnotation(true)
            .withNonnullAnnotationType("org.jetbrains.annotations.NotNull")
            .withJpaAnnotations(false)
            .withValidationAnnotations(true)
            .withSpringAnnotations(true)
            .withConstructorPropertiesAnnotation(true)
            .withConstructorPropertiesAnnotationOnPojos(true)
            .withConstructorPropertiesAnnotationOnRecords(true)
            .withFluentSetters(false)
            .withDaos(false)
            .withPojos(true);

        Target target = new Target()
            .withPackageName("edu.java.dao.jooq.generated")
            .withDirectory("scrapper/src/main/java");

        Configuration configuration = new Configuration()
            .withJdbc(new Jdbc()
                .withDriver("org.postgresql.Driver")
                .withUrl("jdbc:postgresql://localhost:5432/scrapper")
                .withUser("postgres")
                .withPassword("postgres")
            )
            .withGenerator(
                new Generator()
                    .withDatabase(database)
                    .withGenerate(options)
                    .withTarget(target)
            );

        GenerationTool.generate(configuration);
    }
}
