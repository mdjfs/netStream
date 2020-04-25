const MB = 1024 * 1024; // 1 MB 
const blockSize = MB * 10; // 10 MB 
const frameSize = MB / 4; // 256 kb

function fileCodificator(file, callbackStatus, callbackFinish){
    var arrayOfFrames = [];
    var fileSize = file.size;
    var offset = 0;

    function getBase64(buffer){
        var totalbytes = new Uint8Array(buffer);
        var blockbytes = new Uint8Array(frameSize);
        var pointer_bytes = 0; 
        var acum_bytes = 0; 
        var actual_part = Math.floor(offset / frameSize);
        var total_part = Math.floor(fileSize / frameSize); 
        if(frameSize < fileSize){
            for(pointer_bytes; pointer_bytes <= totalbytes.byteLength ; pointer_bytes++){
                blockbytes[acum_bytes] = totalbytes[pointer_bytes]; 
                if(pointer_bytes % frameSize == 0 && pointer_bytes != 0){
                    acum_bytes = 0;
                    actual_part++;
                    returnStatus("Encoding|" + actual_part/total_part * 100 + "%");
                    arrayOfFrames.push("("+actual_part+"/"+total_part+")"+btoa(String.fromCharCode.apply(null, blockbytes)));
                }
                acum_bytes++;
            }
        }
        else{
            arrayOfFrames.push("(1/1)"+btoa(String.fromCharCode.apply(null, totalbytes)));
        }
    }

    function ReadFileBlock(callback){
        var self = this;
        var ReaderBlock = null;
        var readEventHandler = function(e){
            if(offset >= fileSize){
                returnStatus("Done reading file");
                callbackFinish(arrayOfFrames);
                return;
            }
            if(e.target.error == null){
                getBase64(e.target.result);
                offset += e.target.result.byteLength;
                callback(ReadFileBlock);
            }
            else
            {
                returnStatus("Read error: " + e.target.error);
                return;
            }
        }
        ReaderBlock = function(_offset, _file){
            var r = new FileReader();
            var  blob = _file.slice(_offset, blockSize + _offset); 
            r.onload = readEventHandler;
            r.readAsArrayBuffer(blob);
        }
        ReaderBlock(offset, file);
    }

    function returnStatus(message){
        callbackStatus(message);
    }

    ReadFileBlock(ReadFileBlock);



}