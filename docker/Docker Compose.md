## Docker Compose
Compose 是一个用于定义和运行多容器的Docker应用的工具。
使用Compose，你可以在一个配置文件（yaml格式）中配置你应用的服务，然后使用一个命令，即可创建并启动配置中引用的所有服务。


### 基本步骤
1.使用dockerfile 定义应用程序环境，以便在任何地方重现该环境		
2.在docker-compose.yml中定义组成应用程序的服务，以便各个服务在一个隔离环境中一起运行		
3.运行docker-compose up命令，启动整个应用程序		 


### 实例
根据docker入门实例：https://github.com/miozeng/ms/blob/master/ms-eureka-server/docker.md	
dockercompose实例在以上实例的基础上添加docker-compose.yml	
执行命令docker-compose up 会自动构建镜像并启动容器		

### docker-compose.yml常用命令
image   
指定镜像名称或者镜像id，如果该镜像在本地不存在，Compose会尝试pull下来。   
示例：   
image: java   
   
build     
指定Dockerfile文件的路径。可以是一个路径，例如：  
build: ./dir   
也可以是一个对象，用以指定Dockerfile和参数，例如：
```xml
build: 
  context: ./dir
  dockerfile: Dockerfile-alternate
  args:
    buildno: 1
```
   
command
覆盖容器启动后默认执行的命令。  
示例：   
command: bundle exec thin -p 3000   
也可以是一个list，类似于Dockerfile总的CMD指令，格式如下：   
command: [bundle, exec, thin, -p, 3000]    
     
links   
链接到其他服务中的容器。可以指定服务名称和链接的别名使用SERVICE:ALIAS 的形式，或者只指定服务名称，示例：    
```xml
web:
  links:
   - db
   - db:database
   - redis
```
    
external_link       
表示链接到docker-compose.yml外部的容器，甚至并非Compose管理的容器，特别是对于那些提供共享容器或共同服务。格式跟links类似，示例：     
```xml  
external_links:    
 - redis_1
 - project_db_1:mysql
 - project_db_1:postgresql
```

ports
暴露端口信息。使用宿主端口:容器端口的格式，或者仅仅指定容器的端口（此时宿主机将会随机指定端口），类似于docker run -p ，示例：
```xml
ports:  
 - "3000"  
 - "3000-3005"  
 - "8000:8000"  
 - "9090-9091:8080-8081"  
 - "49100:22"  
 - "127.0.0.1:8001:8001"  
 - "127.0.0.1:5000-5010:5000-5010"  
```
 
expose 
  
暴露端口，只将端口暴露给连接的服务，而不暴露给宿主机，示例：
```xml
expose:
 - "3000"
 - "8000"
 ```
     
volumes
卷挂载路径设置。可以设置宿主机路径 （HOST:CONTAINER） 或加上访问模式 （HOST:CONTAINER:ro）。示例：
```xml
volumes:
  - /var/lib/mysql
  - /opt/data:/var/lib/mysql
  - ./cache:/tmp/cache
  - ~/configs:/etc/configs/:ro
  - datavolume:/var/lib/mysql
  ```
      
volumes_from  
从另一个服务或者容器挂载卷。可以指定只读或者可读写，如果访问模式没有指定，则默认是可读写。示例：   
```xml   
volumes_from:
 - service_name
 - service_name:ro
 - container:container_name
 - container:container_name:rw
 ```
     
environment   
设置环境变量。可以使用数组或者字典两种方式。只有一个key的环境变量可以在运行Compose的机器上找到对应的值，这有助于加密的或者特殊主机的值。示例：
```xml
environment:
  RACK_ENV: development
  SHOW: 'true'
  SESSION_SECRET:

environment:
  - RACK_ENV=development
  - SHOW=true
  - SESSION_SECRET
  ```
      
env_file   
从文件中获取环境变量，可以为单独的文件路径或列表。如果通过 docker-compose -f FILE 指定了模板文件，则 env_file 中路径会基于模板文件路径。如果有变量名称与 environment 指令冲突，则以envirment 为准。示例：   
```xml
env_file: .env

env_file:
  - ./common.env
  - ./apps/web.env
  - /opt/secrets.env
  ```
     
extends  
继承另一个服务，基于已有的服务进行扩展。   
   
net   
设置网络模式。示例：  
```xml
net: "bridge"
net: "host"
net: "none"
net: "container:[service name or container name/id]"
```
   
dns   
配置dns服务器。可以是一个值，也可以是一个列表。示例：  
```xml
dns: 8.8.8.8
dns:
  - 8.8.8.8
  - 9.9.9.9
  ```
     
dns_search    
配置DNS的搜索域，可以是一个值，也可以是一个列表，示例：  
```xml
dns_search: example.com
dns_search:
  - dc1.example.com
  - dc2.example.com
```

### compose常用命令

大部分命令都可以运行在一个或多个服务上。如果没有特别的说明，命令则应用在项目所有的服务上  

执行 docker-compose [COMMAND] --help 查看具体某个命令的使用说明。  

基本的使用格式是  

docker-compose [options] [COMMAND] [ARGS...]    
选项  
--verbose 输出更多调试信息。  
--version 打印版本并退出。  
-f, --file FILE 使用特定的 compose 模板文件，默认为 docker-compose.yml。  
-p, --project-name NAME 指定项目名称，默认使用目录名称。  
命令

build   
构建或重新构建服务。  
服务一旦构建后，将会带上一个标记名，例如 web_db。   
可以随时在项目目录下运行 docker-compose build 来重新构建服务。   
   
help  
获得一个命令的帮助。   
  
kill  
通过发送 SIGKILL 信号来强制停止服务容器。支持通过参数来指定发送的信号，例如  
```xml
$ docker-compose kill -s SIGINT
```
logs  
查看服务的输出。  

port   
打印绑定的公共端口。   

ps   
列出所有容器。   
  
pull  
拉取服务镜像。  

rm  
删除停止的服务容器。   
  
run   
在一个服务上执行一个命令。   
   
例如：  
  
$ docker-compose run ubuntu ping docker.com  
将会启动一个 ubuntu 服务，执行 ping docker.com 命令。   
   
默认情况下，所有关联的服务将会自动被启动，除非这些服务已经在运行中。  
  
该命令类似启动容器后运行指定的命令，相关卷、链接等等都将会按照期望创建。  
  
两个不同点：  
给定命令将会覆盖原有的自动运行命令；  
不会自动创建端口，以避免冲突。  
如果不希望自动启动关联的容器，可以使用 --no-deps 选项，例如   
$ docker-compose run --no-deps web python manage.py shell   
将不会启动 web 容器所关联的其它容器。   
  
scale
设置同一个服务运行的容器个数。   
通过 service=num 的参数来设置数量。例如：   
$ docker-compose scale web=2 worker=3   
    
start   
启动一个已经存在的服务容器。  

stop     
停止一个已经运行的容器，但不删除它。通过 docker-compose start 可以再次启动这些容器。   

up   
构建，（重新）创建，启动，链接一个服务相关的容器。    
链接的服务都将会启动，除非他们已经运行。    
默认情况， docker-compose up 将会整合所有容器的输出，并且退出时，所有容器将会停止。  
如果使用 docker-compose up -d ，将会在后台启动并运行所有的容器。  
默认情况，如果该服务的容器已经存在， docker-compose up 将会停止并尝试重新创建他们（保持使用 volumes-from 挂载的卷），以保证 docker-compose.yml 的修改生效。如果你不想容器被停止并重新创建，可以使用 docker-compose up --no-recreate。如果需要的话，这样将会启动已经停止的容器。   

环境变量   
环境变量可以用来配置 Compose 的行为。      
以DOCKER_开头的变量和用来配置 Docker 命令行客户端的使用一样。如果使用 boot2docker , $(boot2docker shellinit) 将会设置它们为正确的值。     
COMPOSE_PROJECT_NAME     
设置通过 Compose 启动的每一个容器前添加的项目名称，默认是当前工作目录的名字。    
COMPOSE_FILE    
设置要使用的 docker-compose.yml 的路径。默认路径是当前工作目录。  
DOCKER_HOST  
设置 Docker daemon 的地址。默认使用 unix:///var/run/docker.sock，与 Docker 客户端采用的默认值一致。  
DOCKER_TLS_VERIFY  
如果设置不为空，则与 Docker daemon 交互通过 TLS 进行。  
DOCKER_CERT_PATH   
配置 TLS 通信所需要的验证（ca.pem、cert.pem 和 key.pem）文件的路径，默认是 ~/.docker 。   

 
### 使用Docker Compose部署项目  
#### 1.准备工作      
将项目打包成Docker镜像。可以使用Docker的Maven插件将项目打包成Docker镜像，也可以使用Dockerfile或者其他方式打包。  
Maven插件  
* 父pom中添加插件管理：

```xml
    <pluginManagement>
      <plugins>
        <plugin>
          <groupId>com.spotify</groupId>
          <artifactId>docker-maven-plugin</artifactId>
          <version>0.4.12</version>
        </plugin>
      </plugins>
    </pluginManagement>
```

* 然后在子项目添加以下内容：

```xml
  <build>
    <plugins>
      <plugin>
        <groupId>com.spotify</groupId>
        <artifactId>docker-maven-plugin</artifactId>
        <executions>
          <execution>
            <id>build-image</id>
            <phase>package</phase>
            <goals>
              <goal>build</goal>
            </goals>
          </execution>
        </executions>
        <configuration>
          <imageName>${docker.image.prefix}/${project.artifactId}</imageName>
          <baseImage>java</baseImage>
          <entryPoint>["java", "-jar", "/${project.build.finalName}.jar"]</entryPoint>
          <resources>
            <resource>
              <targetPath>/</targetPath>
              <directory>${project.build.directory}</directory>
              <include>${project.build.finalName}.jar</include>
            </resource>
          </resources>
        </configuration>
      </plugin>
    </plugins>
  </build>
```

* 在父项目所在路径下，执行命令：


```shell
mvn clean package
```

这样，项目就会为各个项目打包成jar包，并且自动制作成Docker镜像。

#### 2.编写docker-compose.yml文件

#### 3.启动测试与故障排查

我们在docker-compose.yml所在路径执行：

```shell
docker-compose up
```




## Compose网络设置
Compose网络特性仅适用于Compose-file2格式，在version1不支持。		
docker-Conpose为你的应用创建一个网络，Docker-Compose为每个Service创建一个容器，并加入到这个网络中，被网络中的其它容器访问和发现。此容器能够以容器名称作为hostname标识，被其它容器访问。

### 基本概率
注：Docker-compose命令中通过--project-name或-p参数来指定项目名称，也可以指定环境变量COMPOSE_PROJECT_NAME	
示例说明docker-compose创建资源			
```shell
version: '2'

services:
  web:
    build: .
    ports:
      - "8000:8000"
  db:
    image: postgres
```
在myapp目录使用命令docker-compose up，将执行以下几步：		
+ 创建一个名为myapp_default的网络		
+ 以服务名（web、db）分别创建两个容器，添加到myapp_default网络中		
+ 容器以服务名（web、db）作为其hostname相互访问，如web可以postgres://db:5432访问db容器		

由于Service与容器对应，以下将Docker-Compose创建的容器简称为Service。		

### 更新容器			
使用docker-compose up将停止并移除旧容器，重建新容器。    
任何连接到旧容器的（其它容器的）连接将关闭，然后检测条件，根据容器名称查找并重建连接。  

Links
默认情况下，Docker-Compose下的Servcie之间是可通过服务名称(容器名称，也即hostname)相互访问的。		
links只不过为Service提供了以别名访问其它Service的一种方式。		
```shell
version: '2'

services:
  web:
    build: .
    links:
      - "db:database"
  db:
    image: postgres
```

### 跨主机网络
当把Compose部分到Swarm集群时，可利用Swarm内置的overlay驱动实现不同主机间容器访问，无需改变Compose文件或应用程序代码。		

如何将Comnpose应用到Swarm集群？

networks关键字指定自定义网络

compose file中顶级networks关键字，可以用来创建更加复杂的网络拓扑，指定自定义网络驱动和选项也可以用它来连接外部创建（非Compose创建）的网络。 每个Service可通过service内部的networks关键字来指定它要使用的顶级网络。 
给Service指定networks的好处是，实现网络隔离或连接			
``` shell
version: '2'

services:
  proxy:
    build: ./proxy
    networks:
      - front
  app:
    build: ./app
    networks:
      - front
      - back
  db:
    image: postgres
    networks:
      - back

networks:
  front:
    # Use a custom driver
    driver: custom-driver-1
  back:
    # Use a custom driver which takes special options
    driver: custom-driver-2
    driver_opts:
      foo: "1"
      bar: "2"
Networks can be c
```

### 配置默认网络
``` shell
version: '2'

services:
  web:
    build: .
    ports:
      - "8000:8000"
  db:
    image: postgres

networks:
  default:
    # Use a custom driver
    driver: custom-driver-1
```
这样就可以为该应用指定自定义的网络驱动    

### 使用已存在的网络
``` shell
networks:
  default:
    external:
      name: my-pre-existing-network
```


## 综合实例
#### 编写代码
以ms-eureka-client，ms-eureka-server为例
1.使用maven插件构建docker镜像，在各个pom.xml添加一下内容

``` xml
  <plugin>
	        <groupId>com.spotify</groupId>
	        <artifactId>docker-maven-plugin</artifactId>
	        <executions>
	          <execution>
	            <id>build-image</id>
	            <phase>package</phase>
	            <goals>
	              <goal>build</goal>
	            </goals>
	          </execution>
	        </executions>
	        <configuration>
	          <imageName>mytest/${project.artifactId}/${project.version}</imageName>
	          <baseImage>java</baseImage>
	          <entryPoint>["java", "-jar", "/${project.build.finalName}.jar"]</entryPoint>
	          <resources>
	            <resource>
	              <targetPath>/</targetPath>
	              <directory>${project.build.directory}</directory>
	              <include>${project.build.finalName}.jar</include>
	            </resource>
	          </resources>
	        </configuration>
	      </plugin>
```

2.所有微服务添加服务
``` xml
 eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
```
3.每个项目执行mvn clean package docker:build

4.编写 docker-compose.yml
``` shell
version: '2' 
services: 
  ms-eureka-server:   
    image: mytest/ms-eureka-server:0.0.1-SNAPSHOT
    ports:
    - 8761:8761 
  ms-eureka-client:   
    image: mytest/ms-eureka-client:0.0.1-SNAPSHOT
    links:
     - ms-eureka-server:discovery 
```
#### 启动测试
执行
```shell
docker-compose up
```

#### 简化docker-compose.yml
version2file format中，同一个compose工程中的所有服务恭喜一个隔离网络，可使用服务名称来作为主机名来发现其他服务，所以可以简化成一下格式
``` shell
version: '2' 
services: 
  discovery:   
    image: mytest/ms-eureka-server:0.0.1-SNAPSHOT
    ports:
    - 8761:8761 
  ms-eureka-client:   
    image: mytest/ms-eureka-client:0.0.1-SNAPSHOT
```


### docker spring cloud 高可用实践
使用项目ms-dicovery-eureka与ms-eureka-client        
1.修改ms-eureka-client
``` java
 eureka:
  client:
    serviceUrl:
      defaultZone: http://peer1:8761/eureka/, http://peer2:8761/eureka/
```

2. 每个项目执行mvn clean package docker:build    

3编写 docker-compose.yml
``` xml
version: '2' 
services: 
  peer1:   
    image: mytest/ms-dicovery-eureka:0.0.1-SNAPSHOT
    ports:
     - 8761:8761
    environment:
      -spring.profiles.active=peer1 
  peer2:   
    image: mytest/ms-dicovery-eureka:0.0.1-SNAPSHOT
    ports:
     - 8762:8762
    environment:
      -spring.profiles.active=peer2 
  ms-eureka-client:   
    image: mytest/ms-eureka-client:0.0.1-SNAPSHOT
```

#### 启动测试
执行
```shell
docker-compose up
```

执行以下命令可以为微服务动态扩容，
```shell
docker-compose scale ms-eureka-client=3
```
如果有多个微服务
```shell
docker-compose scale ms-eureka-client=3    ms-eureka-client2=3    ms-eureka-client3=3 
```
