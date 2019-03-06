import React from 'react'
import SimpleButton from '../../common/SimpleButton'
import {Button, ModalBody, ModalFooter} from 'reactstrap'

class FirstStep extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            render: (this.props.render) ? this.props.render : false
        };
    }

    onNextPage() {
        this.props.onNextPage();
    }

    onCancel() {
        this.props.onCancel();
    }

    setRender(render) {
        this.setState({...this.state,render:render})
    }

    render() {
        if (this.state.render)
            return (
                <div>
                    <ModalBody>
                        <div>
                            <div className="text-center animated fadeIn">
                            <span>
                                <img alt="bitcoin box" width={150}
                                     src="https://s3-sa-east-1.amazonaws.com/static.crypfy/img/plan-take-profit.svg"/>
                            </span>
                                <h1 className="text-center margin-top-20">Adicionar Intenção de
                                    Movimentação de Lucro
                                </h1>
                                <div className="centered-box">
                                    <p className="margin-top-20">
                                        Ao criar uma intenção de movimentação de lucro, a plataforma movimentará o lucro
                                        solicitado para sua carteira (Saldo Disponível). Este processo passa por um
                                        processamento e pode levar tempo para ser processado.
                                    </p>
                                </div>
                            </div>
                        </div>
                    </ModalBody>
                    <ModalFooter>
                        <Button color="secondary" onClick={this.onCancel.bind(this)}>Cancelar</Button>
                        <SimpleButton onClick={this.onNextPage.bind(this)} ref="buttonNext" key="buttonNext"
                                      className="btn btn-primary">
                            Prosseguir
                        </SimpleButton>
                    </ModalFooter>
                </div>
            );
        else
            return null;
    }
}

export default FirstStep;