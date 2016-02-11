package org.eclipse.mat.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Properties;

/**
 * @since 0.8
 */
public final class MessageUtil {

    private static final MessageUtil instance = new MessageUtil();

    private final Properties props = new Properties();

    private final String messageMaps =
        "#.\\org.eclipse.mat.api\\src\\org\\eclipse\\mat\\internal\\messages.properties  \n" +
        "ClassSpecificNameResolverRegistry_Error_CreateResolver=Error while creating name resolver ''{0}''\n" +
        "ClassSpecificNameResolverRegistry_Error_MissingObject=No object to resolve class specific name for.\n" +
        "ClassSpecificNameResolverRegistry_Error_MissingSubjects=Resolver without subjects: ''{0}''\n" +
        "ClassSpecificNameResolverRegistry_Error_Resolving=Error resolving name of {0}\n" +
        "ClassSpecificNameResolverRegistry_ErrorMsg_DuringResolving=Error resolving name of {0}\n" +
        "ClassSpecificNameResolverRegistry_ErrorMsg_MissingSubject=Resolver without subjects: ''{0}''\n" +
        "ClassSpecificNameResolverRegistry_ErrorMsg_WhileCreatingResolver=Error while creating name resolver ''{0}''\n" +
        "GCRootInfo_BusyMonitor=Busy Monitor\n" +
        "GCRootInfo_Finalizable=Finalizable\n" +
        "GCRootInfo_JavaLocal=Java Local\n" +
        "GCRootInfo_JNIGlobal=JNI Global\n" +
        "GCRootInfo_JNILocal=JNI Local\n" +
        "GCRootInfo_NativeStack=Native Stack\n" +
        "GCRootInfo_SystemClass=System Class\n" +
        "GCRootInfo_Thread=Thread\n" +
        "GCRootInfo_ThreadBlock=Thread Block\n" +
        "GCRootInfo_Unfinalized=Unfinalized\n" +
        "GCRootInfo_Unreachable=Unreachable\n" +
        "GCRootInfo_Unkown=Unknown\n" +
        "#.\\org.eclipse.mat.hprof\\src\\org\\eclipse\\mat\\hprof\\messages.properties  \n" +
        "AbstractParser_Error_IllegalType=Illegal Type:  {0}\n" +
        "AbstractParser_Error_InvalidHPROFHeader=Invalid HPROF file header.\n" +
        "AbstractParser_Error_NotHeapDump=Not a HPROF heap dump\n" +
        "AbstractParser_Error_UnknownHPROFVersion=Unknown HPROF Version ({0})\n" +
        "AbstractParser_Error_UnsupportedHPROFVersion=Unsupported HPROF Version {0}\n" +
        "HprofIndexBuilder_ExtractingObjects=Extracting objects from {0}\n" +
        "HprofIndexBuilder_Parsing=Parsing {0}\n" +
        "HprofIndexBuilder_Scanning=Scanning {0}\n" +
        "HprofIndexBuilder_Writing=Writing {0}\n" +
        "HprofParserHandlerImpl_Error_ExpectedClassSegment=Error: Found instance segment but expected class segment (see FAQ): 0x{0}\n" +
        "HprofParserHandlerImpl_Error_MultipleClassInstancesExist=multiple class instances exist for {0}\n" +
        "HprofParserHandlerImpl_HeapContainsObjects=Heap {0} contains {1,number} objects\n" +
        "HprofRandomAccessParser_Error_DumpIncomplete=need to create dummy class. dump incomplete\n" +
        "HprofRandomAccessParser_Error_DuplicateClass=Duplicate class: {0}\n" +
        "HprofRandomAccessParser_Error_IllegalDumpSegment=Illegal dump segment {0}\n" +
        "HprofRandomAccessParser_Error_MissingClass=missing fake class {0}\n" +
        "HprofRandomAccessParser_Error_MissingFakeClass=missing fake class\n" +
        "Pass1Parser_Error_IllegalRecordLength=Illegal record length at byte {0}\n" +
        "Pass1Parser_Error_IllegalType=Illegal primitive object array type\n" +
        "Pass1Parser_Error_InvalidHeapDumpFile=Error: Invalid heap dump file.\\n Unsupported segment type {0} at position {1}\n" +
        "Pass1Parser_Error_invalidHPROFFile=(Possibly) Invalid HPROF file: Expected to read another {0,number} bytes, but only {1,number} bytes are available.\n" +
        "Pass1Parser_Error_SupportedDumps=Only 32bit and 64bit dumps are supported.\n" +
        "Pass1Parser_Error_UnresolvedName=Unresolved Name 0x\n" +
        "Pass1Parser_Error_WritingThreadsInformation=Error writing threads information\n" +
        "Pass1Parser_Info_WroteThreadsTo=Wrote threads call stacks to {0}\n" +
        "Pass1Parser_Error_NoHeapDumpIndexFound=Parser found {0} HPROF dumps in file {1}. No heap dump index {2} found. See FAQ.\n" +
        "Pass1Parser_Info_UsingDumpIndex=Parser found {0} HPROF dumps in file {1}. Using dump index {2}. See FAQ.\n" +
        "Pass2Parser_Error_HandleMustCreateFakeClassForName=handler must create fake class for {0}\n" +
        "Pass2Parser_Error_HandlerMustCreateFakeClassForAddress=handler must create fake class for 0x{0}\n" +
        "Pass2Parser_Error_InsufficientBytesRead=Insufficient bytes read for instance at {0}\n" +
        "#.\\org.eclipse.mat.report\\src\\org\\eclipse\\mat\\report\\internal\\messages.properties  \n" +
        "ConsoleProgressListener_Label_Subtask=Subtask:\n" +
        "ConsoleProgressListener_Label_Task=Task:\n";

    public static String format(String message, Object... objects) {
        String pMsg = (String) instance.props.get(message);
        if (pMsg != null) {
            return MessageFormat.format(pMsg, objects);
        }
        return message;
    }

    private MessageUtil() {
        try {
            InputStream inputStream = new ByteArrayInputStream(
                messageMaps.getBytes(Charset.forName("UTF-8")));
            props.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("MessageUtil initialization failed.", e);
        }
    }
}
