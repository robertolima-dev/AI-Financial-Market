<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">

    <style type="text/css">
    </style>
</head>
<body style="margin:0; padding:0; background-color:#f1f1f1;">
<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#F2F2F2">
    <tr>
        <td align="center" valign="top">
            <div style="padding:30px;">
                <div style="text-align:center;margin-top:20px;">
                <span>
                    <img style="display:block;margin:0 auto;line-height:20px;margin-top:6px;" width="110"
                         src="https://s3-sa-east-1.amazonaws.com/static.crypfy/img/crypfy-logo.png" alt="Crypfy"/>
                </span>
                </div>
                <div style="width:600px;margin:0 auto;background:#fff;border-radius:2px;border:1px solid #ddd;margin-top:30px;">
                    <div style="padding:20px;border-bottom:1px solid #ddd;text-align: center;">
                        <h1 style="font-family: Trebuchet MS, Helvetica, sans-serif;font-weight:300;color:#444444;font-size:22px;">
                            Nova Solicitação de Movimentação de Lucro
                        </h1>
                    </div>
                    <div style="padding:30px;text-align:center;">
                       <span>
                                <img src="https://s3-sa-east-1.amazonaws.com/static.crypfy/img/plan-take-profit.png"
                                     alt="bitcoin wallet" width="170"/>
                            </span>
                        <span style="font-family:Trebuchet MS, Helvetica, sans-serif;font-weight:300;font-size:14px;line-height: 24px;color:#6b6b6b;margin-top:20px;display:block;">
                                Existem Nova(s) solicitação(ões) de Movimentação(ões) de lucro em status <strong>Aguardando Processamento</strong>
                            </span>
                        <table style="width:100%;font-family: Trebuchet MS, Helvetica, sans-serif;margin-top:40px;">
                            <thead>
                            <tr>
                                <th style="font-size:12px;color:#888;" align="left">PLANO</th>
                                <th style="font-size:12px;color:#888;" align="left">INÍCIO</th>
                                <th style="font-size:12px;color:#888;" align="left">FIM</th>
                                <th style="font-size:12px;color:#888;" align="right">VALOR MOVIMENTAÇÃO</th>
                            </tr>
                            </thead>
                            <tbody>
                            <#list planTakeProfitRequests as p>
                            <tr>
                                <td style="padding:10px 0;color:#444444;font-size:14px;" align="left">
                                ${p.planName}
                                </td>
                                <td style="padding:10px 0;color:#444444;font-size:14px;" align="left">
                                ${p.startDate}
                                </td>
                                <td style="padding:10px 0;color:#444444;font-size:14px;" align="left">
                                ${p.endDate}
                                </td>
                                <td style="padding:10px 0;color:#444444;font-size:14px;" align="right">
                                    R$ ${p.amount}
                                </td>
                            </tr>
                            </#list>
                            </tbody>
                        </table>
                        <div style="width:100%;border-top:1px dotted #ddd;padding-bottom:10px;">
                            <span style="float:right;font-family: Trebuchet MS, Helvetica, sans-serif;font-size:14px;font-weight:700;margin-top:10px;">R$ ${totalRequestedAmount}</span>
                        </div>
                    </div>
                </div>
                <span style="font-family:Trebuchet MS, Helvetica, sans-serif;font-weight:300;font-size:14px;line-height: 30px;color:#6b6b6b;margin-top:30px;display:block;">
                                Caso tenha dúvidas, acesse nossa <a href="http://suporte.crypfy.com" target="_blank">central de ajuda</a>
                            </span>
            </div>
        </td>
    </tr>
</table>
</body>
</html>