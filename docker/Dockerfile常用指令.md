## dockerfile常用命令
指令的一般格式为指令名称 参数 。     
1.FROM    
支持三种格式：  
```xml
FROM <image>  
FROM <image>:<tag>  
FROM <image>@<digest>  
```
FROM指令必须指定且需要在Dockerfile其他指令的前面，指定的基础image可以是官方远程仓库中的，也可以位于本地仓库。后续的指令都依赖于该指令指定的image。当在同一个Dockerfile中建立多个镜像时，可以使用多个FROM指令。   
    
2.MAINTAINER     
格式为：  
```xml
MAINTAINER <name>
```
用于指定维护者的信息。
     
3.RUN  
支持两种格式：  
RUN <command>    
或 RUN ["executable", "param1", "param2"]。    
RUN <command> 在shell终端中运行命令，在Linux中默认是/bin/sh -c 在Windows中是 cmd /s /c    
RUN ["executable", "param1", "param2"] 使用exec执行。指定其他终端可以通过该方式操作，例如：RUN ["/bin/bash", "-c", "echo hello"] ，该方式必须使用["]而不能使用[']，因为该方式会被转换成一个JSON 数组。    
    
4.CMD   
支持三种格式：   
CMD ["executable","param1","param2"] (推荐使用)  
CMD ["param1","param2"] (为ENTRYPOINT指令提供预设参数)   
CMD command param1 param2 (在shell中执行)    
CMD指令的主要目的是为执行容器提供默认值。每个Dockerfile只有一个CMD命令，如果指定了多个CMD命令，那么只有一条会被执行，如果启动容器的时候指定了运行的命令，则会覆盖掉CMD指定的命令。    
   
5.LABEL   
格式为：   
LABEL <key>=<value> <key>=<value> <key>=<value> ...   
为镜像添加元数据。使用 "和 \ 转换命令行，示例：  
```xml
LABEL "com.example.vendor"="ACME Incorporated"  
LABEL com.example.label-with-value="foo"   
LABEL version="1.0"   
LABEL description="This text illustrates \   
that label-values can span multiple lines."   
```
    
6.EXPOSE   
格式为：   
EXPOSE <port> [<port>...]   
为Docker容器设置对外的端口号。在启动时，可以使用-p选项或者-P选项。   
示例：   
映射一个端口示例EXPOSE port1# 相应的运行容器使用的命令   
docker run -p port1 image# 也可以使用-P选项启动   
docker run -P image   
映射多个端口示例EXPOSE port1 port2 port3# 相应的运行容器使用的命令   
docker run -p port1 -p port2 -p port3 image# 还可以指定需要映射到宿主机器上的某个端口号     
docker run -p host_port1:port1 -p host_port2:port2 -p host_port3:port3 image  
    
7.ENV   
格式为：   
```xml
ENV <key> <value>  
ENV <key>=<value> ...
```
指定环境变量，会被后续RUN指令使用，并在容器启动后，可以通过docker inspect查看这个环境变量，也可以通过docker run --env <key>=<value> 来修改环境变量   
示例：   
设置环境变量JAVA_HOME   
ENV JAVA_HOME /path/to/java   
   
8.ADD   
格式为：  
```xml
ADD <src>... <dest>
ADD ["<src>",... "<dest>"]
```
从src目录复制文件到容器的dest。其中src可以是Dockerfile所在目录的相对路径，也可以是一个URL，还可以是一个压缩包  
注意：   
src必须在构建的上下文内，不能使用例如：ADD ../somethine /something ，因为docker build 命令首先会将上下文路径和其子目录发送到docker daemon   
如果src是一个URL，同时dest不以斜杠结尾，dest将会被视为文件，src对应内容文件将会被下载到dest  
如果src是一个URL，同时dest以斜杠结尾，dest将被视为目录，src对应内容将会被下载到dest目录   
如果src是一个目录，那么整个目录其下的内容将会被拷贝，包括文件系统元数据   
如果文件是可识别的压缩包格式，则docker会自动解压   
    
9.COPY  
格式为：  
```xml
COPY <src>... <dest>
COPY ["<src>",... "<dest>"] （shell中执行）
```  
复制本地端的src到容器的dest。和ADD指令类似，COPY不支持URL和压缩包。    

10.ENTRYPOINT  
格式为：  
```xml
ENTRYPOINT ["executable", "param1", "param2"]
ENTRYPOINT command param1 param2
```
指定Docker容器启动时执行的命令，可以多次设置，但是只有最后一个有效。   
    
11.VOLUME   
格式为：  
VOLUME ["/data"]   
使容器中的一个目录具有持久化存储数据的功能，该目录可以被容器本身使用，也可以共享给其他容器。当容器中的应用有持久化数据的需求时可以在Dockerfile中使用该指令。   
   
12.USER   
格式为：  
USER 用户名   
设置启动容器的用户，默认是root用户。   
   
13.WORKDIR  
格式为：   
WORKDIR /path/to/workdir   
切换目录指令，类似于cd命令，对RUN、CMD、ENTRYPOINT生效。   
    
14.ARG   
格式为：  
```xml
ARG <name>[=<default value>]
```
ARG指令定义一个变量。   
    
15.ONBUILD    
格式为： 
```xml
ONBUILD [INSTRUCTION]
``` 
指定当建立的镜像作为其他镜像的基础时，所执行的命令    
    
16.其他   
STOPSINGAL  
HEALTHCHECK  
SHELL   


17.使用Dockerfile构建Docker镜像    
1.创建文件，命名为Dockerfile    
```yml  
# 基于哪个镜像  
FROM java:8   
# 将本地文件夹挂载到当前容器  
VOLUME /tmp   
# 拷贝文件到容器，也可以直接写成ADD ms-eureka-server-0.0.1-SNAPSHOT.jar /app.jar   
ADD ms-eureka-server-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'   
# 开放8761端口  
EXPOSE 8761  
# 配置容器启动后执行的命令   
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]  
```
构建docker镜像，执行：      
docker build -t miozeng/ms-eureka-server .        
格式：docker build -t 标签名称 Dockerfile的相对位置  
构建成功：Successfully built a7cc6f4de088 。  
启动镜像   
docker run -p 8761:8761 miozeng/ms-eureka-server  
访问http://Docker宿主机IP:8761 ，我们会发现Eureka能够正常被访问。     


dockerfile入门实例：https://github.com/miozeng/ms/blob/master/ms-eureka-server/docker.md