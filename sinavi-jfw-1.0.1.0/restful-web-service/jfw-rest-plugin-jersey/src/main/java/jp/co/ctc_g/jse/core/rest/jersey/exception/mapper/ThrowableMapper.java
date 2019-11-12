/*
 * Copyright (c) 2013 ITOCHU Techno-Solutions Corporation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.co.ctc_g.jse.core.rest.jersey.exception.mapper;

import java.util.ResourceBundle;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import jp.co.ctc_g.jfw.core.internal.InternalMessages;
import jp.co.ctc_g.jse.core.rest.entity.ErrorMessage;
import jp.co.ctc_g.jse.core.rest.jersey.util.ErrorCode;
import jp.co.ctc_g.jse.core.rest.jersey.util.ErrorMessages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * このクラスは、{@link Throwable}の例外ハンドラです。
 * </p>
 * <p>
 * 処理中に{@link Throwable}が発生した場合は、
 * このハンドラが例外を補捉し、
 * クライアントへJSON形式で以下の例外メッセージを返します。
 * <pre class="brush:java">
 * {
 *  "id": "74c86ba2-6e3f-4d23-bb2f-b2f1eb168d4e",
 *  "status": 500,
 *  "code": "E-REST-SERVER#999",
 *  "message": "予期しない例外が発生しました。"
 * }
 * </pre>
 * </p>
 * <p>
 * この例外ハンドラのプライオリティは{@link Priorities#USER}です。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
@Provider
@Priority(Priorities.USER)
public class ThrowableMapper implements ExceptionMapper<Throwable> {

    private static final Logger L = LoggerFactory.getLogger(ThrowableMapper.class);
    private static final ResourceBundle R = InternalMessages.getBundle(ThrowableMapper.class);

    /**
     * デフォルトコンストラクタです。
     */
    public ThrowableMapper() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public Response toResponse(final Throwable exception) {
        if (L.isDebugEnabled()) {
            L.debug(R.getString("D-REST-JERSEY-MAPPER#0012"));
        }
        ErrorMessage error = ErrorMessages.create()
            .status(500)
            .id()
            .code(ErrorCode.UNKNOWN_ERROR.code())
            .resolve()
            .get();
        L.error(error.log(), exception);
        return Response.status(500)
            .entity(error)
            .type(MediaType.APPLICATION_JSON)
            .build();
    }
}
