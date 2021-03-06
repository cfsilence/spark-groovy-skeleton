import groovy.json.JsonOutput

import static spark.Spark.*

class Bootstrap {

    static ConfigObject loadConfig(String environment) {
        def configFile = new File("src/main/groovy/conf/config-${environment}.groovy")
        if( !configFile.exists() ) {
            throw new Exception("Config file missing.\n\t-> You have attempted to load a config file from '${configFile.canonicalPath}' but none was found.\n\t-> Please see '${configFile.canonicalPath.replace('config-'+environment+'.groovy', 'config-template.groovy')}' in that directory,\n\t-> make a copy, rename it for this environment and populate it as necessary.")
        }
        return new ConfigSlurper(environment).parse(configFile.toURI().toURL())
    }

    static void main(String[] args) {

        def environment = System.getProperty('environment') ?: 'dev'
        def config = loadConfig(environment)

        staticFileLocation('/public')
        staticFiles.expireTime(10)
        port(9000)

        before "*", { req, res ->
            res.header("Access-Control-Allow-Headers", "Authorization, Content-Type");
            res.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            res.header("Access-Control-Allow-Origin", "*");
        }

        options "*/*", { req, res ->

        }

    }

}