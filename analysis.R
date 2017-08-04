
#sparkfun data
mads = c()
iqrs = c()
sds = c()
count<-0
filenames <- list.files(path="/Users/eugene/Documents/Programming/iot_data/ts/")  
for (i in filenames){  
  #x <- sd(csv_frame$V1/1000)
  #if(x==0.0){
  #  print(i)
  #}
  csv_frame<-try(read.csv(paste("/Users/eugene/Documents/Programming/iot_data/ts/",i,sep = ""),header=FALSE))
  if (!inherits(csv_frame, 'try-error')) {
    sds <- c(sds, sd(csv_frame$V1))
    mads <- c(mads, mad(csv_frame$V1))
    iqrs <- c(iqrs,IQR(csv_frame$V1))
  }
}
mean(mads)
median(mads)
mean(iqrs)
median(iqrs)
a<-table(mads)
a[names(a)==0]
length(mads)
b<-table(sds)
b[names(b)==0]
length(sds)
b

#thingspeak data
mads = c()
iqrs = c()
sds = c()
count<-0
filenames <- list.files(path="/Users/eugene/Documents/Programming/ThingSpeak/ts/")  
for (i in filenames){  
  csv_frame<-try(read.csv(paste("/Users/eugene/Documents/Programming/ThingSpeak/ts/",i,sep = ""),header=FALSE))
  if (!inherits(csv_frame, 'try-error')) {
    sds <- c(sds, sd(csv_frame$V1))
    mads <- c(mads, mad(csv_frame$V1))
    iqrs <- c(iqrs,IQR(csv_frame$V1))
  }
}
mean(mads)
median(mads)
mean(iqrs)
median(iqrs)
a<-table(mads)
a[names(a)==0]
length(mads)
b<-table(sds)
b[names(b)==0]
length(sds)
b

#calculations for fitbit
18/19
2/19
(17+2+4)/(17+3+5)


#runkeeper data
mads = c()
iqrs = c()
sds = c()
count<-0
filenames <- list.files(path="/Users/eugene/Documents/Programming/OpenHumans/ts_runkeeper/")  
for (i in filenames){  
  csv_frame<-try(read.csv(paste("/Users/eugene/Documents/Programming/OpenHumans/ts_runkeeper/",i,sep = ""),header=FALSE))
  if (!inherits(csv_frame, 'try-error')) {
    sds <- c(sds, sd(csv_frame$V1))
    mads <- c(mads, mad(csv_frame$V1))
    iqrs <- c(iqrs,IQR(csv_frame$V1))
  }
}
mean(mads)
a<-table(mads)
a[names(a)==0]
a
length(sds)
b<-table(sds)
b[names(b)==0]
length(sds)
b
a

#healthkit data
mads = c()
iqrs = c()
sds = c()
count<-0
filenames <- list.files(path="/Users/eugene/Documents/Programming/OpenHumans/ts_hk/")  
for (i in filenames){  
  csv_frame<-try(read.csv(paste("/Users/eugene/Documents/Programming/OpenHumans/ts_hk/",i,sep = ""),header=FALSE))
  if (!inherits(csv_frame, 'try-error')) {
    sds <- c(sds, sd(csv_frame$V1))
    mads <- c(mads, mad(csv_frame$V1))
    iqrs <- c(iqrs,IQR(csv_frame$V1))
  }
}
mean(mads)
a<-table(mads)
a[names(a)==0]
a
length(sds)
b<-table(sds)
b[names(b)==0]
length(sds)
b
a