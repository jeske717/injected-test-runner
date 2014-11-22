package ${packageName};

@dagger.Module(overrides = true, library = true, injects = {${testSubject}})
public class ${className} {

    <#list mocks as mock>
        private ${mock.qualifiedName} ${mock.name};
    </#list>

    public ${className}(
        <#list mocks as mock>
            ${mock.qualifiedName} ${mock.name},
        </#list>
    Object ignored) {
        <#list mocks as mock>
            this.${mock.name} = ${mock.name};
        </#list>
    }

    <#list mocks as mock>
        @dagger.Provides
        <#list mock.annotations as annotation>
            ${annotation.toString()}
        </#list>
        public ${mock.qualifiedName} ${mock.name}() {
            return this.${mock.name};
        }

    </#list>

}
